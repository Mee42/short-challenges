package dev.mee42.emccue.freeze

import java.util.*

fun main() {
    val board = Board()
    var currentPlayer = Team.A
    while (true) {
        play(board, currentPlayer)
        currentPlayer = if (currentPlayer == Team.A) Team.B else Team.A
        val pieceCount = Team.values().map { team ->
            team to board.listify().count { it.second.piece?.team == team }
        }.toMap()
        if (pieceCount.values.contains(0)) {
            val winner = Team.values().first { pieceCount[it] == 0 }.other()
            println("Team $winner is the winner!")
            break
        }
    }
}

val scanner = Scanner(System.`in`)

fun play(board: Board, currentPlayer: Team) {
    print("Turn: $currentPlayer. CurrentBoard:\n$board\nPoint to use? ")
    val (point, action, direction) = chooseAction(board, currentPlayer)
    when (action) {
        Action.MOVE -> {
            // easy, just move to that location
            board.movePoint(point, board[point].piece!!.direction)
        }
        Action.ROTATE -> {
            board[point].piece!!.direction = direction!!
        }
    }
}