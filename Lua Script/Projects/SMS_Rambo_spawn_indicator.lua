local spawnStartAddress = 0x07C0
local spawnColors = {
	[11] = 0xFF700A8F
}

local spawnTextColors = {
	[11] = 0x00BF6AD9
}

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

function drawTargetBox(targetAddress, i)
	local targetType = mainmemory.readbyte(targetAddress)
	if (targetType == 0) then
		return
	end

	local targetX = mainmemory.readbyte(targetAddress+8)
	local targetY = mainmemory.readbyte(targetAddress+6)
	local targetColor = 0xFFFFFFFF

	local backColor = spawnColors[targetType]
	if (backColor == nil) then
		backColor = 0x80000000
	end


	local targetTimer1 = mainmemory.readbyte(targetAddress+33)
	local targetTimer2 = mainmemory.readbyte(targetAddress+34)
	local targetTimer = math.max(targetTimer1, targetTimer2)
	local timerColor = 0xFFFFFFFF
	if (targetTimer <= 15) then
		backColor = 0xFFd92400
	end


	local displayOnlyTimer = true
	if (displayOnlyTimer) then
		local targetHex = string.format("%d", targetTimer)
		local textX = targetX - 10
		local textY = targetY - 20
		displayTextBox(textX, textY, 20, 15, targetHex, timerColor, backColor)
	else
		local targetHex = string.format("%x|%d", targetType, targetTimer)
		local textX = targetX - 20
		local textY = targetY - 20
		displayTextBox(textX, textY, 42, 15, targetHex, targetColor, backColor)
	end
end

function drawBulletBox(targetAddress, i)
	local targetType = mainmemory.readbyte(targetAddress)
	if (targetType == 0) then
		return
	end

	local targetX = mainmemory.readbyte(targetAddress+8)
	local targetY = mainmemory.readbyte(targetAddress+6)
	local targetColor = 0xFFFFFFFF

	local backColor = spawnColors[targetType]
	if (backColor == nil) then
		backColor = 0x80000000
	end

	displayTextBox(targetX, targetY, 17, 15, targetType, targetColor, backColor)
end

function endScreenCounts()
	local currentLevel = mainmemory.readbyte(0x0123)
	if (currentLevel > 6) then
		return
	end

	local screenProgressAddress = 0x019B
	local screenProgress = mainmemory.readbyte(screenProgressAddress)
	if (screenProgress < 31) then
		return
	end


	local count = mainmemory.readbyte(0x01B5)
	

	local countColor = 0xFFFFFFFF
	if (count >= 16) then
		countColor = 0xFF41f271
	end

	if (currentLevel == 6) then
		local leftCount = mainmemory.readbyte(0x021F)
		local rightCount = mainmemory.readbyte(0x0220)

		if (leftCount >= 10 and rightCount >= 10) then
			local midCount = mainmemory.readbyte(0x0221)
			displayTextBox(115, 5, 20, 15, midCount, countColor, 0xC0000000)
			return
		end

		displayTextBox(65, 5, 20, 15, leftCount, countColor, 0xC0000000)
		displayTextBox(165, 5, 20, 15, rightCount, countColor, 0xC0000000)
		return
	end

	displayTextBox(80, 5, 110, 15, "Gate Count: " .. count, countColor, 0xC0000000)

	if (count >= 16) then	
		local gateShotsAddress = 0x0210
		local gateShots = mainmemory.readbyte(gateShotsAddress)
		displayTextBox(80, 20, 110, 15, "Gate HP: " .. gateShots, 0xFFf2c941, 0xC0000000)
	end

end

function mainLoop()

	local backgroundColor = 0xA0000000
	-- gui.drawRectangle(0,0,40,85, backgroundColor,backgroundColor)

	local count = 4
	for i=1, count, 1 do 
		local targetAddress = spawnStartAddress + (48 * (i-1))

		drawTargetBox(targetAddress, i)

		local targetType = mainmemory.readbyte(targetAddress)
		local targetName = ""
		if targetType > 0 then 
			targetName = "target"
		end

		local textY = ((i-1) * 20) + 4
		
		local deadText = ""

		local color = spawnTextColors[targetType]
		if (color == nil) then
			color = 0x00FFFFFF
		end

		-- show the spawn on the side
		-- gui.text(5, textY, i .. " -> " .. targetName .. " " .. deadText, 0xFF000000+color)
	end

	endScreenCounts()

	

	-- local count = 12
	-- local bulletAddressStart = 0x0460
	-- for i=1, count, 1 do 
	-- 	local targetAddress = bulletAddressStart + (48 * (i-1))
	-- 	drawTargetBox(targetAddress, i)
	-- end

end

while true do
	mainLoop()
	emu.frameadvance()
end