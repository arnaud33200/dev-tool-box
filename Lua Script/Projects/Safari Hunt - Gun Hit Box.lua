function displayTextBox(x, y, w, h, text, color)
	rectX = x
	rectY = y

	if rectX < 0 then rectX = 0 end
	if (rectX + w) > 255 then rectX = 255 - w end
	if rectY < 0 then rectY = 0 end
	if (rectY + h) > 190 then rectY = 190 - h end

	gui.drawRectangle(rectX,rectY,w,h, 0xFFFFFFFF,0x80000000)
	gui.drawString(rectX, rectY, text, color)
end

local showText = true

function mainLoop()
	local originalX = joypad.get()["P1 X"] 
	local cursorX = originalX * 2
	local cursorY = joypad.get()["P1 Y"]
	local cursorSize = 6
	local centerSize = 2
	local cursorColor = 0xFFFFFFFF

	-- Trigger pushed --> big yellow cursor
	if joypad.get()["P1 Trigger"] then 
		cursorColor = 0xFFFFD200 
		centerSize = 4
		cursorSize = cursorSize + 2
	end

	local hitSize = 8
	if mainmemory.readbyte(0x0135) == 1 then
		hitSize = 16
	end

	local hitY = cursorY - hitSize
	local h = hitSize * 2
	local hitX = cursorX - hitSize
	local w = hitSize * 2
	
	gui.drawRectangle(hitX, hitY, w, h, 0xA0FFFFFF, 0x20000000)

	gui.drawLine(cursorX-cursorSize, cursorY, cursorX-centerSize, cursorY, cursorColor)
	gui.drawLine(cursorX+centerSize, cursorY, cursorX+cursorSize, cursorY, cursorColor)
	gui.drawLine(cursorX, cursorY-cursorSize, cursorX, cursorY-centerSize, cursorColor)
	gui.drawLine(cursorX, cursorY+centerSize, cursorX, cursorY+cursorSize, cursorColor)

	if showText ~= true then
		return
	end

	text = originalX .. "/" .. cursorY
	color = 0xFFFFFFFF
	width = (string.len(text) * 8) + 2
	rectX = cursorX - (width / 2)
	rectY = cursorY + 18
	displayTextBox(rectX, rectY, width, 15, text, color)

end

while true do
	mainLoop()
	emu.frameadvance()
end