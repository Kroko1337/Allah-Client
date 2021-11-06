package net.minecraft.scoreboard;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScoreboardSaveData extends WorldSavedData
{
    private static final Logger LOGGER = LogManager.getLogger();
    private Scoreboard scoreboard;
    private NBTTagCompound delayedInitNbt;

    public ScoreboardSaveData()
    {
        this("scoreboard");
    }

    public ScoreboardSaveData(String name)
    {
        super(name);
    }

    public void setScoreboard(Scoreboard scoreboardIn)
    {
        this.scoreboard = scoreboardIn;

        if (this.delayedInitNbt != null)
        {
            this.read(this.delayedInitNbt);
        }
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     */
    public void read(NBTTagCompound nbt)
    {
        if (this.scoreboard == null)
        {
            this.delayedInitNbt = nbt;
        }
        else
        {
            this.readObjectives(nbt.getList("Objectives", 10));
            this.readScores(nbt.getList("PlayerScores", 10));

            if (nbt.contains("DisplaySlots", 10))
            {
                this.readDisplayConfig(nbt.getCompound("DisplaySlots"));
            }

            if (nbt.contains("Teams", 9))
            {
                this.readTeams(nbt.getList("Teams", 10));
            }
        }
    }

    protected void readTeams(NBTTagList tagList)
    {
        for (int i = 0; i < tagList.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = tagList.getCompound(i);
            String s = nbttagcompound.getString("Name");

            if (s.length() > 16)
            {
                s = s.substring(0, 16);
            }

            ScorePlayerTeam scoreplayerteam = this.scoreboard.createTeam(s);
            String s1 = nbttagcompound.getString("DisplayName");

            if (s1.length() > 32)
            {
                s1 = s1.substring(0, 32);
            }

            scoreplayerteam.setDisplayName(s1);

            if (nbttagcompound.contains("TeamColor", 8))
            {
                scoreplayerteam.setColor(TextFormatting.getValueByName(nbttagcompound.getString("TeamColor")));
            }

            scoreplayerteam.setPrefix(nbttagcompound.getString("Prefix"));
            scoreplayerteam.setSuffix(nbttagcompound.getString("Suffix"));

            if (nbttagcompound.contains("AllowFriendlyFire", 99))
            {
                scoreplayerteam.setAllowFriendlyFire(nbttagcompound.getBoolean("AllowFriendlyFire"));
            }

            if (nbttagcompound.contains("SeeFriendlyInvisibles", 99))
            {
                scoreplayerteam.setSeeFriendlyInvisiblesEnabled(nbttagcompound.getBoolean("SeeFriendlyInvisibles"));
            }

            if (nbttagcompound.contains("NameTagVisibility", 8))
            {
                Team.EnumVisible team$enumvisible = Team.EnumVisible.getByName(nbttagcompound.getString("NameTagVisibility"));

                if (team$enumvisible != null)
                {
                    scoreplayerteam.setNameTagVisibility(team$enumvisible);
                }
            }

            if (nbttagcompound.contains("DeathMessageVisibility", 8))
            {
                Team.EnumVisible team$enumvisible1 = Team.EnumVisible.getByName(nbttagcompound.getString("DeathMessageVisibility"));

                if (team$enumvisible1 != null)
                {
                    scoreplayerteam.setDeathMessageVisibility(team$enumvisible1);
                }
            }

            if (nbttagcompound.contains("CollisionRule", 8))
            {
                Team.CollisionRule team$collisionrule = Team.CollisionRule.getByName(nbttagcompound.getString("CollisionRule"));

                if (team$collisionrule != null)
                {
                    scoreplayerteam.setCollisionRule(team$collisionrule);
                }
            }

            this.loadTeamPlayers(scoreplayerteam, nbttagcompound.getList("Players", 8));
        }
    }

    protected void loadTeamPlayers(ScorePlayerTeam playerTeam, NBTTagList tagList)
    {
        for (int i = 0; i < tagList.tagCount(); ++i)
        {
            this.scoreboard.addPlayerToTeam(tagList.getString(i), playerTeam.getName());
        }
    }

    protected void readDisplayConfig(NBTTagCompound compound)
    {
        for (int i = 0; i < 19; ++i)
        {
            if (compound.contains("slot_" + i, 8))
            {
                String s = compound.getString("slot_" + i);
                ScoreObjective scoreobjective = this.scoreboard.getObjective(s);
                this.scoreboard.setObjectiveInDisplaySlot(i, scoreobjective);
            }
        }
    }

    protected void readObjectives(NBTTagList nbt)
    {
        for (int i = 0; i < nbt.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbt.getCompound(i);
            IScoreCriteria iscorecriteria = IScoreCriteria.INSTANCES.get(nbttagcompound.getString("CriteriaName"));

            if (iscorecriteria != null)
            {
                String s = nbttagcompound.getString("Name");

                if (s.length() > 16)
                {
                    s = s.substring(0, 16);
                }

                ScoreObjective scoreobjective = this.scoreboard.addScoreObjective(s, iscorecriteria);
                scoreobjective.setDisplayName(nbttagcompound.getString("DisplayName"));
                scoreobjective.setRenderType(IScoreCriteria.EnumRenderType.getByName(nbttagcompound.getString("RenderType")));
            }
        }
    }

    protected void readScores(NBTTagList nbt)
    {
        for (int i = 0; i < nbt.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbt.getCompound(i);
            ScoreObjective scoreobjective = this.scoreboard.getObjective(nbttagcompound.getString("Objective"));
            String s = nbttagcompound.getString("Name");

            if (s.length() > 40)
            {
                s = s.substring(0, 40);
            }

            Score score = this.scoreboard.getOrCreateScore(s, scoreobjective);
            score.setScorePoints(nbttagcompound.getInt("Score"));

            if (nbttagcompound.contains("Locked"))
            {
                score.setLocked(nbttagcompound.getBoolean("Locked"));
            }
        }
    }

    public NBTTagCompound write(NBTTagCompound compound)
    {
        if (this.scoreboard == null)
        {
            LOGGER.warn("Tried to save scoreboard without having a scoreboard...");
            return compound;
        }
        else
        {
            compound.setTag("Objectives", this.objectivesToNbt());
            compound.setTag("PlayerScores", this.scoresToNbt());
            compound.setTag("Teams", this.teamsToNbt());
            this.fillInDisplaySlots(compound);
            return compound;
        }
    }

    protected NBTTagList teamsToNbt()
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (ScorePlayerTeam scoreplayerteam : this.scoreboard.getTeams())
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.putString("Name", scoreplayerteam.getName());
            nbttagcompound.putString("DisplayName", scoreplayerteam.getDisplayName());

            if (scoreplayerteam.getColor().getColorIndex() >= 0)
            {
                nbttagcompound.putString("TeamColor", scoreplayerteam.getColor().getFriendlyName());
            }

            nbttagcompound.putString("Prefix", scoreplayerteam.getPrefix());
            nbttagcompound.putString("Suffix", scoreplayerteam.getSuffix());
            nbttagcompound.putBoolean("AllowFriendlyFire", scoreplayerteam.getAllowFriendlyFire());
            nbttagcompound.putBoolean("SeeFriendlyInvisibles", scoreplayerteam.getSeeFriendlyInvisiblesEnabled());
            nbttagcompound.putString("NameTagVisibility", scoreplayerteam.getNameTagVisibility().internalName);
            nbttagcompound.putString("DeathMessageVisibility", scoreplayerteam.getDeathMessageVisibility().internalName);
            nbttagcompound.putString("CollisionRule", scoreplayerteam.getCollisionRule().name);
            NBTTagList nbttaglist1 = new NBTTagList();

            for (String s : scoreplayerteam.getMembershipCollection())
            {
                nbttaglist1.appendTag(new NBTTagString(s));
            }

            nbttagcompound.setTag("Players", nbttaglist1);
            nbttaglist.appendTag(nbttagcompound);
        }

        return nbttaglist;
    }

    protected void fillInDisplaySlots(NBTTagCompound compound)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        boolean flag = false;

        for (int i = 0; i < 19; ++i)
        {
            ScoreObjective scoreobjective = this.scoreboard.getObjectiveInDisplaySlot(i);

            if (scoreobjective != null)
            {
                nbttagcompound.putString("slot_" + i, scoreobjective.getName());
                flag = true;
            }
        }

        if (flag)
        {
            compound.setTag("DisplaySlots", nbttagcompound);
        }
    }

    protected NBTTagList objectivesToNbt()
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (ScoreObjective scoreobjective : this.scoreboard.getScoreObjectives())
        {
            if (scoreobjective.getCriteria() != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.putString("Name", scoreobjective.getName());
                nbttagcompound.putString("CriteriaName", scoreobjective.getCriteria().getName());
                nbttagcompound.putString("DisplayName", scoreobjective.getDisplayName());
                nbttagcompound.putString("RenderType", scoreobjective.getRenderType().getRenderType());
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    protected NBTTagList scoresToNbt()
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (Score score : this.scoreboard.getScores())
        {
            if (score.getObjective() != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.putString("Name", score.getPlayerName());
                nbttagcompound.putString("Objective", score.getObjective().getName());
                nbttagcompound.putInt("Score", score.getScorePoints());
                nbttagcompound.putBoolean("Locked", score.isLocked());
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        return nbttaglist;
    }
}
