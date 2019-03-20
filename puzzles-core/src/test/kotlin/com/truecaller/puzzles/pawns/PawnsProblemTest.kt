package com.truecaller.puzzles.pawns

import net.jqwik.api.*
import net.jqwik.api.constraints.IntRange
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import java.lang.IllegalArgumentException

class PawnsProblemTest {
    private val size = 10

    @Property
    internal fun `finds the correct solution for a 10x10 board`(@ForAll @IntRange(min = 1, max = 10) x: Int,
                                                                @ForAll @IntRange(min = 1, max = 10) y: Int) {
        val board = PawnsProblem(size)

        assertThat(board.solve(x, y).cells.toSet()).hasSize(size * size)
    }

    @Property
    @FromData("invalidPositions")
    internal fun `does not accept invalid initial position`(@ForAll x: Int, @ForAll y: Int) {
        assertThatThrownBy { PawnsProblem(size).solve(x, y) }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Data
    fun invalidPositions(): Iterable<Tuple.Tuple2<Int, Int>>  = Table.of(
        Tuple.of(-1, 1),
        Tuple.of(1, -1),
        Tuple.of(size + 1, 1),
        Tuple.of(1, size + 1),
        Tuple.of(-1, -1),
        Tuple.of(size + 1, size + 1)
    )
}
