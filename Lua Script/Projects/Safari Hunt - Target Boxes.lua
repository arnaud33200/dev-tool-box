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

function drawGunHitBox()
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

	text = originalX*2 .. "/" .. cursorY
	color = 0xFFFFFFFF
	width = (string.len(text) * 8) + 2
	rectX = cursorX - (width / 2)
	rectY = cursorY + 18
	displayTextBox(rectX, rectY, width, 15, text, color)

end

function getValueIfZero(addr, value)
	valueRead = mainmemory.readbyte(addr)
	if valueRead == 0 then return value else return valueRead end
end

function drawTargetBox(addr, color, text)	
	local x = mainmemory.readbyte(addr+9)
	local y = mainmemory.readbyte(addr+7)

	if x == 0 and y == 0 then return end

	local cursorX = x
	local cursorY = y

	y = y + mainmemory.read_s8(addr+11)
	local h = getValueIfZero(addr+12, 5)

	x = x + mainmemory.read_s8(addr+13)
	local w = getValueIfZero(addr+14, 5)

	gui.drawRectangle(x, y, w, h, 0xFF000000+color, 0xC0000000+color)
	-- gui.drawRectangle(x-22, y-11, 44, 47, 0xFF000000+color, 0xC0000000+color)

	local boxText = text
	gui.drawString(x+3, y-21, boxText)

	local dirText = mainmemory.readbyte(addr+32) .. 
		"(" .. mainmemory.readbyte(addr+33) .. ")" ..
		mainmemory.read_s8(addr+35)
	gui.drawString(x-15, y+h, dirText, 0xFF000000)

	-- TARGET CURSOR DRAWING
	local cursorSize = 2
	local centerSize = 0
	local cursorColor = 0xFFFFFFFF
	gui.drawLine(cursorX-cursorSize, cursorY, cursorX-centerSize, cursorY, cursorColor)
	gui.drawLine(cursorX+centerSize, cursorY, cursorX+cursorSize, cursorY, cursorColor)
	gui.drawLine(cursorX, cursorY-cursorSize, cursorX, cursorY-centerSize, cursorColor)
	gui.drawLine(cursorX, cursorY+centerSize, cursorX, cursorY+cursorSize, cursorColor)
end

while true do

	targets = {0x0530, 0x0560, 0x0590, 0x05C0, 0x05F0, 0x0620}
	colors = {0x00e25bfa, 0x00a091fc, 0x005ae491, 0x00d4f134, 0x00f1b134, 0x00fb6a5b}
	targetTypes = { [0] = "",
		-- Jungle
		[25] = "Duck", [26] = "Fish", [24] = "Rabit",
		-- Forest
		[30] = "Bear", [2] = "apple", [27] = "Parrot", [28] = "Armadillo",
		-- Jungle
		[29] = "Spider", [32] = "Web", [31] = "Bat", [33] = "Monkey", [34] = "Panther"
	 }

	for i=1, 6, 1 do 
		targetAddress = targets[i] 
		color = colors[i]
		drawTargetBox(targetAddress, colors[i], "" .. i)

		targetType = mainmemory.readbyte(targetAddress)
		dead = mainmemory.readbyte(targetAddress+21)
		deadText = ""
		if dead == 1 then 
			deadText = "(X)" 
		else
			local x = mainmemory.readbyte(targetAddress+9)
			local y = mainmemory.readbyte(targetAddress+7)
			deadText = "(" .. x .. "/" .. y .. ")"
		end

		textY = 65 + ((i-1) * 20)
		-- local targetName = targetTypes[targetType]
		local targetName = targetType
		gui.text(0, textY, i .. " -> " .. targetName .. " " .. deadText, 0xFF000000+color)
	end

	gui.drawString(188, 10, "Time: " .. mainmemory.readbyte(0x0218))
	gui.drawString(181, 25, "Index: " .. mainmemory.readbyte(0x0217))

	local rngVal = mainmemory.readbyte(0x01ED)
	-- local nextLocation = rngVal & 3
	local nextLocation = bit.band(rngVal, 3)
	gui.drawString(181, 40, "Local: " .. nextLocation)

	local rngHex = string.format("%x", rngVal)
	gui.text(200, 20, "RNG: " .. string.upper(rngHex))

	drawGunHitBox()

	emu.frameadvance()
end