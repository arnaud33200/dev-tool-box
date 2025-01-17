function getNumberOfDigits(n)
    return #tostring(n)
end

function displayTextBox(x, y, w, h, text, color, backColor)
	rectX = x
	rectY = y

	if rectX < 0 then rectX = 0 end
	if (rectX + w) > 255 then rectX = 255 - w end
	if rectY < 0 then rectY = 0 end
	if (rectY + h) > 190 then rectY = 190 - h end

	gui.drawRectangle(rectX,rectY,w,h, 0xFFFFFFFF, backColor)
	gui.drawString(rectX, rectY, text, color)
end

function drawTarget(xPos, yPos)
	local size = 6
	local center = 2
	local color = 0xFFFFFFFF

	gui.drawLine(xPos-size, yPos, xPos-center, yPos, color)
	gui.drawLine(xPos+center, yPos, xPos+size, yPos, color)
	gui.drawLine(xPos, yPos-size, xPos, yPos-center, color)
	gui.drawLine(xPos, yPos+center, xPos, yPos+size, color)
end

function mainLoop()
	local xPos = mainmemory.readbyte(0x012D)
	local yPos = mainmemory.readbyte(0x012B)
	drawTarget(xPos, yPos)

	local donaldState = mainmemory.readbyte(0x0120)

	local xGlobalPos = mainmemory.read_s16_le(0x0135)
	local digitCount = getNumberOfDigits(xGlobalPos)
	local textWidth = (digitCount*8)+1
	
	local textBackColor = 0xA0000000
	if (donaldState >= 2 and donaldState <= 3) then
		textBackColor = 0xD0399c16
	end

	displayTextBox(xPos-(textWidth/2), yPos, textWidth, 15, xGlobalPos, 0xFFFFFFFF, textBackColor)

end

while true do
	mainLoop()
	emu.frameadvance()
end