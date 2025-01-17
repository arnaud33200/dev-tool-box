function drawHitBox(address)
	local type = mainmemory.readbyte(address)
	if type == 0 then
		return
	end

	local hitWidth = 30
	local hitHeight = 30

	-- local hitWidth = mainmemory.readbyte(address+0x000A)
	-- local hitHeight = hitWidth

	local rectX = mainmemory.readbyte(address+5) - (hitWidth/2)
	local rectY = mainmemory.readbyte(address+3) - (hitHeight/2)
	local rectWidth = hitWidth
	local rectHeight = hitHeight

	local rectColor = 0x80000000
	if type == 0x000D then
		rectColor = 0x80108f25
	end

	gui.drawRectangle(
		rectX,rectY,rectWidth,rectHeight, 0xFFFFFFFF,rectColor
	)

	local text = mainmemory.readbyte(address)
	gui.drawString(rectX, rectY, text, 0xFFFFFFFF)
end

function mainLoop()
	local spawnStartAddress = 0x0360
	local spawnCount = 5
	
	for i=0, spawnCount, 1 do
		local spawnAddress = spawnStartAddress + (i*0x0020)
		drawHitBox(spawnAddress)
	
	end

end

while true do
	mainLoop()
	emu.frameadvance()
end