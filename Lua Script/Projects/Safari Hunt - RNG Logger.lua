function mainLoop()
	local rngVal = mainmemory.readbyte(0x01ED)

	local rngHex = string.format("%x", rngVal)
	if string.len(rngHex) == 1 then
		rngHex = "0" .. rngHex
	end
	rngHex = string.upper(rngHex)

	local frame = emu.framecount()

	local cursorX = joypad.get()["P1 X"] 
	local cursorY = joypad.get()["P1 Y"]
	local fire = ""
	if joypad.get()["P1 Trigger"] then fire = "1" end

	local y = mainmemory.readbyte(0x0507)
	local x = mainmemory.readbyte(0x0509)

	local position = ""
	if y ~= 0 or x ~= 0 then
		position = x .. "/" .. y
	end

	-- position = cursorX .. "/" .. cursorY .. " [" .. fire .. "]"

	print(frame .. "\t" .. rngHex .. "\t" .. position)
end


while true do
	mainLoop()
	emu.frameadvance()
end