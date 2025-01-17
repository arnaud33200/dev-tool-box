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


function mainLoop()

	local targetX = mainmemory.readbyte(0x1227)
	local targetY = mainmemory.readbyte(0x1229)

	displayTextBox(targetX, targetY, 20, 20, "A", 0xFFFFFFFF, 0x80000000)

end


while true do
	mainLoop()
	emu.frameadvance()
end