local autoLocateTarget = true
local logShot = false

local targetToExclude = {
	-- [25] = true, -- Duck
	-- [27] = true -- Parrot
}

-- (spawnX, spawnY, waitBeforeShootCount, p1X, p1Y)
local targetShoot = {
	-- Duck
	[25] = {
		[56] = {25, 38, 111},
		[216] = {20, 102, 114},
		[80] = {0, 52, 34}
	},
	-- Fish
	[26] = {
		[136] = {31, 71, 136},
		[72] = {31, 40, 135},
		[16] = {31, 10, 133}
	},
	-- Rabbit
	[24] = {
		[248] = {0, 118, 159},
		[8] = {0, 10, 168},
		[120] = {3, 60, 134}
	},
	-- Bear
	[30] = {
		[8] = {7, 13, 139} -- 8/127 (shoot 5)
	},
	-- Panther
	[34] = {
		[240] = {
			[135] = {3, 106, 146},
			[159] = {0, 121, 170}
		},
		[16] = {
			[135] = {0, 10, 144},
			[159] = {0, 10, 168}
		}
	}
}

-- Y position for side to side targets
-- X would either be 10 or 118
local sideToSideMap = {
 	[27] = 14, -- Parrot
 	[28] = 12, -- ama
 	[31] = 2 -- Batf
}

-- Y position for up to down targets
-- X would stay the target X 
local upToDownMap = {
	[29] = 2, -- Spider
	[33] = 5 -- Monkey
}

local framesBetweenShot = 11

local inputsQueue = { }
local analogQueue = { }
local analogAutoLocationQueue = { }

local targets = {0x0530, 0x0560, 0x0590, 0x05C0, 0x05F0, 0x0620}
local targetStatus = {0, 0, 0, 0, 0, 0}

local currentState = 0

local levelSpawnLog = ""

function addShotInQueue(addr, frame, x, y)
	inputsQueue[frame] = {["P1 Trigger"]=true}

	if autoLocateTarget == false then
		analogQueue[frame+1] = {["P1 X"]=x, ["P1 Y"]=y}
	end

	analogAutoLocationQueue[frame+1] = addr
end

function checkShootTarget(i)

	local addr = targets[i]
	local x = mainmemory.readbyte(addr+9)
	local y = mainmemory.readbyte(addr+7)
	local size = mainmemory.readbyte(addr+12)

	-- if x == 0 and y == 0 then return end

	value = mainmemory.readbyte(addr)
	if value ~= 0 and ((x == 0 and y == 0) or size == 0) then 
		-- skip, we need coordinate
		return 
	end

	previousVal = targetStatus[i]
	targetStatus[i] = value

	if value == 0 or previousVal ~= 0 then return end

	local excludedTarget = targetToExclude[value]
	if excludedTarget ~= nil then
		return -- ignore the target
	end

	local nextFrame = emu.framecount()

	-- Side to Side Targets
	local sideToSideInput = sideToSideMap[value]
	if sideToSideInput ~= nil then
		local shootX = 118
		if x < 10 then shootX = 10 end
		local shootY = y + sideToSideInput
		-- print("side " .. value .. " = " .. y .. " + " .. sideToSideInput)
		addShotInQueue(addr, nextFrame, shootX, shootY)
		return
	end

	-- Up To Down Targets
	local upToDownInput = upToDownMap[value]
	if upToDownInput ~= nil then
		local shootX = x/2
		local shootY = y + upToDownInput
		addShotInQueue(addr, nextFrame, shootX, shootY)
		return 
	end

	local targetMap = targetShoot[value]
	if targetMap == nil then return end
	local nextInput = targetMap[x]
	if nextInput == nil then return end
	-- if logShot then print("Next = " .. nextInput) end

	local yNextInput = nextInput[y]
	if yNextInput ~= nil then
		nextInput = yNextInput
	end

	nextFrame = emu.framecount() + nextInput[1]
	
	-- if autoLocateTarget then
	-- 	inputsQueue[nextFrame] = {["P1 Trigger"]=true} 
	-- 	analogAutoLocationQueue[nextFrame+1] = {addr+9, addr+7}
	-- 	return
	-- end

	local shootX = nextInput[2]
	local shootY = nextInput[3]
	local shotCount = 1
	if value == 30 then shotCount = 5 end
	for i = 1, shotCount, 1 do 
		local frame = nextFrame + ((i-1) * framesBetweenShot)
		addShotInQueue(addr, frame, shootX, shootY)
	end
end

function getTargetBoxSize()
	local hitSize = 8
	if mainmemory.readbyte(0x0135) == 1 then
		hitSize = 16
	end
	return hitSize
end

function adjustPositionY(y)
	local size = getTargetBoxSize()
	if (y - size) < 8 then return 8 + size end
	if (y + size) > 182 then return 182 - size end
	return y
end

function adjustPositionX(x)
	local size = getTargetBoxSize()
	if x < 10 then return 10 end
	-- if (x - size) < 10 then return 10 + size end
	if x > 118 then return 118 end
	-- if (x + size) > 248 then return 248 - size end
	return x
end

function drawCursor(cursorX, cursorY)
	local cursorSize = 6
	local centerSize = 2
	gui.drawLine(cursorX-cursorSize, cursorY, cursorX-centerSize, cursorY, cursorColor)
	gui.drawLine(cursorX+centerSize, cursorY, cursorX+cursorSize, cursorY, cursorColor)
	gui.drawLine(cursorX, cursorY-cursorSize, cursorX, cursorY-centerSize, cursorColor)
	gui.drawLine(cursorX, cursorY+centerSize, cursorX, cursorY+cursorSize, cursorColor)
end

function populateNextInput() 
	if currentState ~= 1 then 
		return
	end

	for i=1, 6, 1 do 
		checkShootTarget(i)
	end
end

levelBeginFrame = 0

while true do

	screenState = mainmemory.readbyte(0x0216)

	if currentState ~= 1 and screenState == 1 then
		levelBeginFrame = emu.framecount()
	end

	if currentState == 1 and screenState ~= 1 then
		endLevelCount = emu.framecount() - levelBeginFrame
		-- print(endLevelCount .. "\t" .. levelSpawnLog)
		levelSpawnLog = ""
		levelBeginFrame = 0
	end

	currentState = screenState

	populateNextInput()

	local nextInput = inputsQueue[emu.framecount()]
	if nextInput ~= nil then
		joypad.set(nextInput)
		inputsQueue[emu.framecount()] = nil
	end

	local nextAnalog = analogQueue[emu.framecount()]
	
	local nextAutoLocation = analogAutoLocationQueue[emu.framecount()]
	if nextAutoLocation ~= nil then
		-- print(nextAutoLocation)
		local addr = nextAutoLocation

		local centerY = mainmemory.readbyte(addr+7) + mainmemory.read_s8(addr+11) + (mainmemory.readbyte(addr+12)/2)
		centerY = adjustPositionY(centerY)

		local centerX = mainmemory.readbyte(addr+9) / 2
		centerX = centerX + mainmemory.read_s8(addr+13) + (mainmemory.readbyte(addr+14)/2)
		centerX = adjustPositionX(centerX)

		-- print ("target location = " .. centerX .. " / " .. centerY)
		-- print ("auto target = " .. centerX .. " / " .. centerY)
		drawCursor(centerX * 2, centerY)

		nextAnalog = { ["P1 X"]=centerX, ["P1 Y"]=centerY }
		analogAutoLocationQueue[emu.framecount()] = nil
	end

	if nextAnalog ~= nil then
		joypad.setanalog(nextAnalog)
		if logShot then print(nextAnalog) end
		analogQueue[emu.framecount()] = nil
	else
		-- give the control back to the user
		joypad.setanalog({["P1 X"]='', ["P1 Y"]=''})
	end

	emu.frameadvance()
end