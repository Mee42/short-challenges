- Game is played on an 8x8 board same as chess or checkers
- There are two teams, A and B, whose pieces have a distinct color
- Each team has 3 pieces on the board at the start of the game
- Pieces have both a location and a direction
- At the start of the game the board is aligned like so

```
    | 0    1    2    3    4    5    6    7    
0:  | .    A↓   .    A↓   .    A↓   .    A↓   
1:  | .    .    .    .    .    .    .    .    
2:  | .    .    .    .    .    .    .    .    
3:  | .    .    .    .    .    .    .    .    
4:  | .    .    .    .    .    .    .    .    
5:  | .    .    .    .    .    .    .    .    
6:  | .    .    .    .    .    .    .    .    
7:  | B↑   .    B↑   .    B↑   .    B↑   .    
```

- Pieces can face in any of the 8 cardinal directions. Compass looks like this:
```
 NW  N  NE
   \ | / 
W ---+--- E
   / | \
 SW  S  SE
```
- On a players turn they can either move one piece to any location in that piece's view
that isn't blocked by another piece OR rotate one piece to face a new direction
- When a piece is moving, if they are blocked by an opposing team's piece, they can land
on and "lock" that piece on the board
- For purposes of moving, "locked" pieces aren't considered (you can go over and through them - like they aren't on the board)
- If a player lands a piece on a space with a "locked" piece that is on their team, they must select one of the original piece starting locations and "respawn" their piece there.