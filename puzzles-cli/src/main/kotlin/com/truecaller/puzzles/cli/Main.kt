package com.truecaller.puzzles.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.truecaller.puzzles.pawns.PawnsProblem
import org.slf4j.LoggerFactory

class PawnsProblemCommand : CliktCommand() {
    companion object {
        private val logger = LoggerFactory.getLogger(PawnsProblemCommand::class.java)
    }

    val size: Int by option(help = "chequerboard size").int().default(10)
    val col: Int by option(help = "initial pawn position - column").int().default(1)
    val row: Int by option(help = "initial pawn position - row").int().default(1)

    override fun run() {
        logger.info("Solving for size=$size, position ($col, $row)")

        val solution = PawnsProblem(size).solve(col, row)

        println(solution.prettyPrint(size))
    }

}

fun main(args: Array<String>) = PawnsProblemCommand().main(args)
