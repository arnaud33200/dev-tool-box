local spawnOneLineLog = false
local logIndexInsteadTarget = true

-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

local targets = {0x0530, 0x0560, 0x0590, 0x05C0, 0x05F0, 0x0620}
local targetStatus = {0, 0, 0, 0, 0, 0}

local currentState = 0

local currentScore = 0

local levelSpawnLog = ""
local levelScoreLog = ""
local levelBeginFrame = 0

-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

function calculateScore()
	local part1 = mainmemory.readbyte(0x0122)
	local part2 = mainmemory.readbyte(0x0123)
	local part3 = mainmemory.readbyte(0x0124)

	local total = part1 * 6.25
	total = total + (math.fmod(part2, 16) * 1000)
	total = total + (math.floor(part2/16) * 10000)
	total = total + part3 * 100000
	return total
end

function checkScoreState()
	local previousScore = currentScore
	currentScore = calculateScore()

	if previousScore ~= currentScore then
		return currentScore
	end
	return nil
end

local currentIndex = -1
function checkNextTargetIndex()
	local previousIndex = currentIndex
	currentIndex = mainmemory.readbyte(0x0217)

	if previousIndex ~= currentIndex then
		return currentIndex
	end
	return nil
end

local currentTargetTimer = 0
function checkNextTargetTimer()
	local previousTimer = currentTargetTimer
	currentTargetTimer = mainmemory.readbyte(0x0218)

	if previousTimer == 0 or previousTimer == 1 then
		return currentTargetTimer
	end
	return nil
end

function checkTargetStates() 
	if currentState ~= 1 then 
		return {}
	end

	-- for i=1, 6, 1 do
	-- ignore 6th since it's just for the fish

	local targetChangedTable = {}
	local hasChanged = false
	for i=1, 6, 1 do 
		addr = targets[i]

		local previousValue = targetStatus[i]
		targetStatus[i] = mainmemory.readbyte(addr)

		local valueToInsert = ""
		if targetStatus[i] ~= 0 and previousValue == 0 then 
			valueToInsert = targetStatus[i]
			hasChanged = true
		elseif targetStatus[i] == 0 and previousValue ~= 0 then
			valueToInsert = 0
			hasChanged = true
		elseif targetStatus[i] > 0 then
			valueToInsert = targetStatus[i]
		end

		if valueToInsert == 2 then
			valueToInsert = 40
		end

		table.insert(targetChangedTable, valueToInsert)
	end

	table.insert(targetChangedTable, hasChanged)

	return targetChangedTable
end

function mainLoop()

	local previousState = currentState 
	currentState = mainmemory.readbyte(0x0216)

	if previousState ~= 1 and currentState == 1 then
		levelBeginFrame = emu.framecount()

		levelSpawnLog = ""
		levelScoreLog = ""
		currentScore = 0
		currentIndex = -1
		currentTargetTimer = 0
	end

	if previousState == 1 and currentState ~= 1 then
		if spawnOneLineLog then
			levelSpawnLog = (emu.framecount() - levelBeginFrame) .. "\t" .. levelSpawnLog
		end
		print(levelSpawnLog)
		return
	end

	local score = nil -- checkScoreState()
	local targetTable = checkTargetStates()
	local index = checkNextTargetIndex()
	local timer = checkNextTargetTimer()

	if spawnOneLineLog then
		-- if table.getn(targetTable) == 0 and index == nil then
		-- -- nothing to log for this frame
		-- 	return
		-- end 
		if logIndexInsteadTarget then
			if index ~= nil then
				levelSpawnLog = levelSpawnLog .. index .. "\t"
			end
		else
			for i=1, table.getn(targetTable), 1 do
				local target = targetTable[i]
				levelSpawnLog = levelSpawnLog .. target .. "\t"
			end
		end
		return
	end

	local logLine = (emu.framecount() - levelBeginFrame) .. "\t"
	
	logLine = logLine .. addLogLine(mainmemory.readbyte(0x0135))
	logLine = logLine .. addLogLine(index)
	logLine = logLine .. addLogLine(timer)
	-- logLine = logLine .. addLogLine(score)

	local targetChanged = targetTable[7]

	if score == nil and index == nil and targetChanged == false then
		-- nothing to log for this frame
		return
	end

	
	for i=1, 6, 1 do
		logLine = logLine .. addLogLine(targetTable[i])
	end

	levelSpawnLog = levelSpawnLog .. logLine .. "\n"
end

function addLogLine(value)
	if value ~= nil then
		return value .. "\t"
	elseif value == "\t" then
		return "\t"
	else
		return "\t"
	end
end

while true do
	mainLoop()
	emu.frameadvance()
end