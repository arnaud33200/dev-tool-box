-- Draw a blue box if the spawn at [address] is an hidden fountain
-- Check for the type of spawn (index 0) only draw if $DD
function drawHiddenFountain(address)
	local type = mainmemory.readbyte(address)
	if type != 0xDD then
		return
	end

	local hitWidth = 30
	local hitHeight = 30
	local rectX = mainmemory.readbyte(address+3) - (hitWidth/2)
	local rectY = mainmemory.readbyte(address+2) - (hitHeight/2)
	local rectWidth = hitWidth
	local rectHeight = hitHeight

	local rectColor = 0x80108f25
	gui.drawRectangle(rectX,rectY,rectWidth,rectHeight, 0xFFFFFFFF,rectColor)

	local text = "Fountain"
	gui.drawString(rectX, rectY, text, 0xFFFFFFFF)
end

-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- MAIN

function mainLoop()
	local spawnStartAddress = 0x0520
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