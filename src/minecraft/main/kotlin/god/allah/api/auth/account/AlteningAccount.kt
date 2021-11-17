package god.allah.api.auth.account

data class AlteningAccount(val name: String, val token: String, val limit: Boolean, val info: HashMap<String, String>)
