package com.truecaller.puzzles.pawns

import org.slf4j.LoggerFactory

data class Cell(val x: Int, val y: Int)
data class Path(val cells: List<Cell>) {
    companion object {
        val empty = Path(emptyList())
    }

    override fun toString(): String = cells.joinToString("\n") { cell -> "(${cell.x},${cell.y})" }

    fun prettyPrint(size: Int): String {
        val string = StringBuilder()
        val length = (size * size).toString().length

        string.append(" ".repeat(length)).append("-".repeat(size * (length + 2) + 1))
        string.append('\n')

        for (y in 1.rangeTo(size)) {
            string.append("${y.toString().padStart(length)}|")
            for (x in 1.rangeTo(size)) {
                val cell = cells.find { it.x == x && it.y == y }
                val index = cells.indexOf(cell)

                if (cell != null) {
                    string.append("${(index + 1).toString().padStart(length + 1)}|")
                } else {
                    string.append("${" ".repeat(length + 1)}|")
                }
            }
            string.append('\n')
            string.append(" ".repeat(length)).append("-".repeat(size * (length + 2) + 1))
            string.append('\n')
        }

        return string.toString()
    }

    fun isComplete(size: Int): Boolean = cells.size == size * size
}

class PawnsProblem(private val size: Int) {
    companion object {
        private val logger = LoggerFactory.getLogger(PawnsProblem::class.java)
    }

    private val cells = 1.rangeTo(size).map { x ->
        1.rangeTo(size).map { y -> Cell(x, y) }.toTypedArray()
    }.toTypedArray()

    private val movements = 1.rangeTo(size).map { x ->
        1.rangeTo(size).map { y ->
            arrayOf(
                Pair(-3, 0),
                Pair(-2, 2),
                Pair(0, 3),
                Pair(2, 2),
                Pair(3, 0),
                Pair(2, -2),
                Pair(0, -3),
                Pair(-2, -2)
            ).filter { (xo, yo) -> isValid(x + xo, y + yo, size) }
                .map { (xo, yo) -> cells[x + xo - 1][y + yo - 1] }
        }.toTypedArray()
    }.toTypedArray()

    fun solve(x: Int, y: Int): Path {
        if (!isValid(x, y, size)) {
            throw IllegalArgumentException("Initial position ($x, $y) is invalid")
        }

        val initial = cells[x - 1][y - 1]

        return solveStep(initial, Path.empty, 0, setOf(initial))
    }

    private fun solveStep(current: Cell, path: Path, retries: Int, visited: Set<Cell>): Path {
        logger.trace("current={} retries={} path={}", current, retries, path)

        if (retries > size) {
            logger.debug("too much retries", current, retries, path)
            return Path.empty
        }

        val nextPath = Path(path.cells + current)

        if (nextPath.isComplete(size)) {
            logger.debug("found a solution: {} {} {}", current, retries, path)
            return nextPath
        }

        val possibleMovements = possibleMovementsSorted(current, visited)

        logger.trace("current={} movements={}", current, possibleMovements)

        for (next in possibleMovements) {
            val solution = solveStep(next, nextPath, retries, visited + next)

            if (solution != Path.empty) {
                return solution
            }

            return retryStep(nextPath, retries)
        }

        return Path.empty
    }

    private fun retryStep(failed: Path, retries: Int): Path {
        logger.trace("path failed: {}", failed)

        val subPath = failed.cells.subList(0, retries + 1)
        val visited = subPath.toSet()
        val next = possibleMovementsSorted(subPath.last(), visited).getOrNull(1) ?: return Path.empty

        logger.trace("will retry with {}", next)

        return solveStep(next, Path(subPath), retries + 1, visited + next)
    }

    private fun possibleMovements(current: Cell, visited: Set<Cell>): List<Cell> =
        movements[current.x - 1][current.y - 1]
            .filter { !visited.contains(it) }

    private fun possibleMovementsSorted(current: Cell, visited: Set<Cell>): List<Cell> =
        possibleMovements(current, visited)
            .sortedBy { possibleMovements(it, visited + current).size }

    private fun isValid(x: Int, y: Int, n: Int): Boolean = !(x < 1 || y < 1 || x > n || y > n)
}
