# Tellus

must fix problem of frames when a lot of particles are present
whe almost all screen is full of particles, the game will lag
problem is not in the update grid function, as if it is filled with air it doesnt lag
also chunks dont run on some particles

problem must be when carrying grid around (in some function calls)? maybe it doesnt send via reference?

also it lags with a lot of stone, that doesnt make any memory calls in update()
