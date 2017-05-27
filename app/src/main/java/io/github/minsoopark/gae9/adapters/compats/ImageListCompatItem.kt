package io.github.minsoopark.gae9.adapters.compats

class ImageListCompatItem {
    enum class Type {
        IMAGE, TIMELINE
    }

    var imageUrl: String? = null
    var timeline: Timeline? = null
    val type: Type

    constructor(imageUrl: String?) {
        this.imageUrl = imageUrl
        this.type = Type.IMAGE
    }

    constructor(timeline: Timeline?) {
        this.timeline = timeline
        this.type = Type.TIMELINE
    }
}