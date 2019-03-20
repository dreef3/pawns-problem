# The Pawns Problem

## Build

```bash
./gradlew installDist
```

## Run

```bash
./puzzles-cli/build/install/puzzles-cli/bin/puzzles-cli --size=10 --col=2 --row=1
```

Program will output the chequerboard filled with numbers where each number is a consecutive step of the pawn.

## Implementation

The described problem is a variant of a well-known [Knight's Tour][1] puzzle.
 At first I tried a naive brute-force implementation with backtracking of all possible routes for a 5x5 board but it obviously didn't scale for a 10x10 variant.
 
The next step was to implement the so-called "Warnsdorff's rule": instead of chasing all possible routes we always choose the one with the least possible options.
This helps to reduce a number of iterations a lot.

With that I headed for a test. It seemed that as the problem is an algorithm I should prove that it actually works. So I used the
property-based approach and the test discovered that the rule described above doesn't always produce a completely filled board.

After that I implemented a retry step that would unwind the solution as early as possible and choose another subroute to pursue.

P.S. I realize that my solution could be done in a more functional-like style, e.g. I could use Optional instead of Path.empty.
Although as I don't usually use this style of programming I decided not to complicate things at the risk of producing the unreadable mess.

[1]: https://en.wikipedia.org/wiki/Knight's_tour
