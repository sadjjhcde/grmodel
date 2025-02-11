# Grow-Reduce Model

Grow-Reduce Model is a kind of Rock-Paper-Scissors cellular automata but with complex states of cells.

# Rules

Constants: GROW_LIMIT = 100, REDUCE_LIMIT = 200, STEP = 10 (variable).

Each cell has three 0-255 integer values: red, green and blue. 
If red value of a current cell more than GROW_LIMIT, then cell increases red values of neighboring cells (left, top, right, bottom) by STEP value . If red value more than REDUCE_LIMIT, then cell reduces green values of neighboring cells by STEP value.  
Similarly green value of a current cell increases green values and reduces blue values of neighboring cells.  
Similarly blue value of a current cell increases blue values and reduces red values of neighboring cells.

# How to use

Just run GRModel class.