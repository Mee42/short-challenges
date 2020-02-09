package dev.mee42.emccue.freeze

enum class Action { ROTATE, MOVE }



// point and action returned are always legal actions
fun chooseAction(board: Board, currentPlayer: Team): Triple<Point, Action, Direction?> {
    val point = pickAPiece(board, currentPlayer)
    val piece = board[point].piece!!
    print("Do you want to rotate or move? dev.mee42.emccue.freeze.Direction: ${piece.direction}: (r/m/cancel) ")
    val action = pickRotateOrMove(
        canMove = board.isPointInRange(point.inDirection(piece.direction)) &&
                board[point.inDirection(piece.direction)].piece?.team != currentPlayer
    )
    if(action == null){
        print("Okay, choose another point $currentPlayer:")
        return chooseAction(board, currentPlayer)
    }
    if(action == Action.MOVE) return Triple(point, action, null)
    val direction = pickDirection()
    if(direction == null) {
        print("Okay, choose another point $currentPlayer:")
        return chooseAction(board, currentPlayer)
    }
    return Triple(point, action, direction)
}

fun pickDirection(): Direction? {
    println("ok, pick a direction using initial abbreviation (ie, NW). North is ↑, 'c' for cancel: ")
    print("dev.mee42.emccue.freeze.Direction: ")
    val strIn = scanner.nextLine()
    if(strIn equalsIgnoreCase  "cancel" || strIn.firstOrNull()?.toUpperCase() == 'C') {
        return null
    }
    // try and parse it
    val option = Direction.values().firstOrNull { it.initials == strIn.toUpperCase() }
    if(option == null) {
        println("dev.mee42.emccue.freeze.Direction must be one of the following: " + Direction.values().map { it.initials }.toString().drop(1).dropLast(1))
        return pickDirection()
    }
    return option
}

fun pickRotateOrMove(canMove: Boolean) : Action? {
    val strIn = scanner.nextLine()
    if(strIn.isBlank()){
        print("String needs to contain some content\nTry again: ")
        return pickRotateOrMove(canMove)
    }
    if(strIn[0].toUpperCase() == 'R') return Action.ROTATE
    if(strIn[0].toUpperCase() == 'M') {
        if(canMove) return Action.MOVE
        print("Can't move into that illegal position\nTry again:")
        return pickRotateOrMove(canMove)
    }
    if(strIn[0].toUpperCase() == 'C') return null
    print("I don't know how to handle \"$strIn\"\nPlease specify 'rotate' or 'move', or `cancel` to deselect the piece): ")
    return pickRotateOrMove(canMove)
}

fun pickAPiece(board: Board, team: Team): Point {
    fun tryAgain() : Point {
        return pickAPiece(board, team)
    }
    val stringIn = scanner.nextLine()
    if(!stringIn.contains(",")) {
        print("Cords need to be two integers separated by integers\nTry again: ")
        return tryAgain()
    }
    val split = stringIn.split(",")
    if(split.size != 2) {
        print("Can not have more then 2 integers\nTry again: ")
        return tryAgain()
    }
    val x = split[0].trim().toIntOrNull() ?: return print("Can't parse ${split[0]} as an integer\nTry again: ")
        .thenReturn(tryAgain())
    val y = split[1].trim().toIntOrNull() ?: return print("Can't parse ${split[1]} as an integer\nTry again: ")
        .thenReturn(tryAgain())
    val point = Point(x, y)
    if(!board.isPointInRange(point)) {
        print("$point is out of range on the board\nTry again: ")
        return tryAgain()
    }
    val square = board[point]
    if(square.piece == null) {
        if(square.frozenPiece?.team == team){
            print("Can't use a frozen piece.\nTry again:")
            return tryAgain()
        }
        if(square.frozenPiece != null) {
            print("Can't use opponent's frozen piece")
            return tryAgain()
        }
        print("There is nothing on that square\nTry again: ")
        return tryAgain()
    }
    // so piece is not null
    if(square.piece!!.team != team) {
        if(square.frozenPiece?.team == team) {
            print("You can't control a frozen piece\nTry again: ")
        } else {
            print("You can't control your opponent's piece\nTry again: ")
        }
        return tryAgain()
    }
    return point // square.piece.team == team
}