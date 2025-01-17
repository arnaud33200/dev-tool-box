local screenHeight = 194
local screenWidth = 258

BlackOverlayPosition = { Left = "0", Right = "1", Hide = "2" }

levels = {
	[1] = {
		[1] = BlackOverlayPosition.Right,
	},
	[2] = {
		[1] = BlackOverlayPosition.Left,
		[10] = BlackOverlayPosition.Hide,
		[13] = BlackOverlayPosition.Right,
		[23] = BlackOverlayPosition.Hide,
		[28] = BlackOverlayPosition.Left,
	},
	[3] = {
		[1] = BlackOverlayPosition.Right,
		[13] = BlackOverlayPosition.Hide,
		[18] = BlackOverlayPosition.Left,
		[26] = BlackOverlayPosition.Hide,
	},
	[4] = {
		[1] = BlackOverlayPosition.Left,
		[10] = BlackOverlayPosition.Hide,
		[14] = BlackOverlayPosition.Left,
		[21] = BlackOverlayPosition.Hide,
		[24] = BlackOverlayPosition.Right,
		[28] = BlackOverlayPosition.Hide,
	},
	[5] = {
		[1] = BlackOverlayPosition.Right,
	},
	[6] = {
		[1] = BlackOverlayPosition.Right,
		[17] = BlackOverlayPosition.Hide,
		[20] = BlackOverlayPosition.Left,
		[26] = BlackOverlayPosition.Hide,
	},

}


function drawZoneOverlay()

	local screenProgress = mainmemory.readbyte(0x019B)
	if (screenProgress >= 31) then
		return
	end
	
	-- local borderWidth = 18
	-- gui.drawRectangle(-1,-1,borderWidth-1,screenHeight-1, 0x00000000, 0xFF000000)
	-- gui.drawRectangle(screenWidth-borderWidth-1,-1,borderWidth-1,screenHeight-1, 0x00000000, 0xFF000000)


	local redZoneWidth = 100
	local redZoneX = (screenWidth/2) - (redZoneWidth/2)
	gui.drawRectangle(redZoneX,-1,redZoneWidth,screenHeight-1, 0x00000000, 0x50ff0066)

	local orangeZoneWidth = 30
	local orangeZoneX = redZoneX - orangeZoneWidth + 1
	local orangeZoneX2 = redZoneX + redZoneWidth - 1
	gui.drawRectangle(orangeZoneX,-1,orangeZoneWidth,screenHeight-1, 0x00000000, 0x80ffcc00)
	gui.drawRectangle(orangeZoneX2,-1,orangeZoneWidth,screenHeight-1, 0x00000000, 0x80ffcc00)

	-- local safeZoneWidth = 16
	local safeZoneWidth = 34
	gui.drawRectangle(-1,-1,safeZoneWidth,screenHeight-1, 0x00000000, 0x802eff69)
	gui.drawRectangle(screenWidth-safeZoneWidth-2,-1,safeZoneWidth,screenHeight-1, 0x00000000, 0x802eff69)
end

local drawPositionFunction = {
	[BlackOverlayPosition.Right] = function() 
		gui.drawRectangle(screenWidth/2,-1,screenWidth/2,screenHeight-1, 0x00000000, 0xFF000000)
		local middleLineWidth = 2
		local middleLineX = (screenWidth/2) - (middleLineWidth/2)
		gui.drawRectangle(middleLineX,-1,middleLineWidth,screenHeight-1, 0x00000000, 0xFFFFFFFF)
	end,
	[BlackOverlayPosition.Left] = function() 
		gui.drawRectangle(-1,-1,screenWidth/2,screenHeight-1, 0x00000000, 0xFF000000)
		local middleLineWidth = 2
		local middleLineX = (screenWidth/2) - (middleLineWidth/2) - 2
		gui.drawRectangle(middleLineX,-1,middleLineWidth,screenHeight-1, 0x00000000, 0xFFFFFFFF)
	end,
	[BlackOverlayPosition.Hide] = function() 
		-- No-op
	end,
}

function drawBlacOverlay()
	local screenProgress = mainmemory.readbyte(0x019B)
	gui.drawString(20, 1, screenProgress, 0xFFFFFFFF)

	if (screenProgress >= 31) then
		return
	end


	local level = mainmemory.readbyte(0x0123)
	local positionMap = levels[level]


	local overlay = BlackOverlayPosition.Left
	local selectProgress = 0

	local sortedKeys = {}
	for key, _ in pairs(positionMap) do
	    table.insert(sortedKeys, key)
	end
	table.sort(sortedKeys)

	for _, key in ipairs(sortedKeys) do
    	local value = positionMap[key]
    	if (key > screenProgress) then
    		break
    	end
    	overlay = value
    	selectProgress = key
	end

	
	-- print("overlay: " .. selectProgress .. " | " .. overlay .. " (" .. screenProgress .. ")")

	drawPositionFunction[overlay]()
end

while true do
	drawZoneOverlay()
	drawBlacOverlay()
	emu.frameadvance()
end