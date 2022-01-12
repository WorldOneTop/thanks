package com.hallym.hlth.models

data class Document(
    var docId: Int,
    var title: String?,
    var content: String,
    var category: String,
    var year: Int,
    var month: Int,
    var day: Int,
    var registeredDate: String,
    var editedDate: String?,
    var fileUrl: String?
) {

    constructor() : this(0, "", "", 0, 0, 0, "", null)

    constructor(
        docId: Int,
        content: String,
        category: String,
        year: Int,
        month: Int,
        day: Int,
        registeredDate: String,
        editedDate: String?
    ) : this(docId, null, content, category, year, month, day, registeredDate, editedDate, null)


}