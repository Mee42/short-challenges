package dev.mee42.emccue.freeze

import java.lang.StringBuilder

class Point (val x: Int, val y: Int) {
    override fun toString(): String {
        return "dev.mee42.emccue.freeze.Point($x,$y)"
    }
    fun inDirection(direction: Direction): Point {
        return Point(x + direction.xUp, y + direction.yUp)
    }

    operator fun rangeTo(point: Point): List<Point> {
        return (x..point.x).flatMap { xValue ->
            (y..point.y).map { yValue ->
                Point(xValue, yValue)
            }
        }
    }
}
enum class Direction(val xUp: Int, val yUp: Int, val char: Char, val initials: String) {
    NORTH(0, -1, '↑', "N"),
    NORTH_EAST(-1, -1, '↗', "NE"),
    EAST(1, 0,'→', "E"),
    SOUTH_EAST(1, 1,'↘', "SE"),
    SOUTH(0, 1,'↓', "S"),
    SOUTH_WEST(-1, 1,'↙', "SW"),
    WEST(-1, 0,'←', "W"),
    NORTH_WEST(-1, -1,'↖',"NW");
}

enum class Team { A, B; fun other(): Team = values().first { it != this } }

class Piece(val team: Team, var direction: Direction) {
    override fun toString(): String {
        return team.toString() + direction.char
    }
}

class Square {
    var piece: Piece? = null
    var frozenPiece: Piece? = null

    override fun toString(): String {
        if(piece == null && frozenPiece == null) return "."
        if(piece == null) {
            return "*" + frozenPiece.toString()
        }
        return piece.toString() + frozenPiece?.toString()?.appendToFront("*").orEmpty()
    }
}

class Board(private val size: Int = 8) {
    private val array = Array(size) { Array(size) { Square() } }
    init {
        (1 until size step 2).map { x ->
            this[Point(x, 0)].piece =
                Piece(
                    Team.A,
                    Direction.SOUTH
                )
        }
        (0 until size step 2).map { x ->
            this[Point(x, size - 1)].piece =
                Piece(
                    Team.B,
                    Direction.NORTH
                )
        }
        this[Point(3, 3)].frozenPiece =
            Piece(
                Team.B,
                Direction.NORTH_EAST
            )
        this[Point(4, 4)].piece =
            Piece(
                Team.B,
                Direction.NORTH_WEST
            )
    }
    fun listify(): List<Pair<Point, Square>> {
        return (Point(
            0,
            0
        )..Point(size - 1, size - 1)).map { it to this[it] }
    }
    override fun toString(): String{
        val b = StringBuilder()
        b.append("    | ")
        for(x in (0 until size)) {
            b.append(x.toString().padEnd(5))
        }
        b.append('\n')
        for(y in (0 until size)) {
            b.append("$y:".padEnd(4) + "| ")
            for(x in (0 until size)) {
                b.append(this[Point(x, y)].toString().padEnd(5))
            }
            b.append('\n')
        }
        return b.toString()
    }
    operator fun get(point: Point): Square {
        return array[point.x][point.y]
    }
    fun isPointInRange(point: Point): Boolean {
        return point.x in (0 until size ) && point.y in (0 until size)
    }
    // ASSUMES VALID MOVE (which is quite a lot - so do your checks first!)
    fun movePoint(point: Point, direction: Direction) {
        // point better work
        val moved = point.inDirection(direction)
        val square1 = this[point]
        val square2 = this[moved]
        val piece = square1.piece!!; square1.piece = null // remove from square1
        if(square2.frozenPiece?.team == piece.team){
            // respawn this piece
            val pieceToRespawn = square2.frozenPiece!!
            square2.frozenPiece = null
            // list of places to put this piece.
            val yRange = if(pieceToRespawn.team == Team.A) (0 until size) else (size - 1 downTo 0)
            val bestPlace = yRange.flatMap { y -> (0 until size).map { x -> x to y} }
                .map { (x,y) -> Point(x, y) }
                .first { this[it].piece == null }

            this[bestPlace].piece = pieceToRespawn
        }
        if(square2.piece != null) {
            if(square2.piece!!.team == piece.team) error("can't freeze yourself - sanity check failed")
            // freeze it!
            square2.frozenPiece = square2.piece
            square2.piece = null
        }
        square2.piece = piece
    }
}