package god.allah.events

import god.allah.api.Resolution
import god.allah.api.event.Event

class Render2DEvent(var resolution: Resolution, val partialTicks: Float): Event()
