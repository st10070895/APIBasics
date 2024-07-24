package com.student.apibasics

data class Loan(
    val loanId: Int,
    val amount: String,
    val memberID: String,
    val message: String
)
data class LoanPost(
    val Amount: String,
    val MemberID: String,
    val Message: String
)
data class LoanGet(
    val MemberID: String
)
data class DeleteResponse(val rowsAffected: Int)

