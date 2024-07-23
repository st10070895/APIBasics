package com.student.apibasics

data class Loan(
    val loanId: Int,
    val amount: String,
    val memberId: String,
    val message: String
)
data class LoanPost(
    val Amount: String,
    val MemberID: String,
    val Message: String
)