local previousState = 0

local saveState = memorysavestate.savecorestate()
local beginFrame = emu.framecount()

local startAfterFrame = 0

function loadState()
	memorysavestate.loadcorestate(saveState)
end

while true do

	local screenState = mainmemory.readbyte(0x0216)
	local round = mainmemory.readbyte(0x0135)
	
	if emu.framecount() == (beginFrame + startAfterFrame) then
		print("Loop " .. startAfterFrame .. " (" .. emu.framecount() .. ")")
		joypad.set({["P1 Trigger"]=true})
	end

	-- if previousState == 1 and screenState == 8 and round == 3 then
	if previousState == 1 and screenState == 8 then
		-- print("load save state!\n")
		startAfterFrame = startAfterFrame + 1
		memorysavestate.loadcorestate(saveState)
	end

	previousState = screenState

	emu.frameadvance()
end