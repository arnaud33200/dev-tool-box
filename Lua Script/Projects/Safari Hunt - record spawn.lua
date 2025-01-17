saveInFile = false

-- ########################################

local file = io.open("safari_hunt_spawns.txt", "w")	
print("write file started")
io.output(file)

local targets = {0x0530, 0x0560, 0x0590, 0x05C0, 0x05F0, 0x0620}
local targetStatus = {0, 0, 0, 0, 0, 0}

local frameCount = 0
local startFrameNumber = 0
local previousState = 0

local allOneLine = ""

function getRecordLine() 
	if previousState ~= 1 then 
		return nil 
	end

	-- record = frame # - target # - each target id - X - Y
	local separator = "\t"
	local line = (frameCount-startFrameNumber) .. separator

	targetAppeared = false
	x = 0
	y = 0

	for i=1, 6, 1 do 
		local addr = targets[i]
		
		value = mainmemory.readbyte(addr)
		previousVal = targetStatus[i]

		targetStatus[i] = value

		if value ~= 0 and previousVal == 0 and targetAppeared == false then
			targetAppeared = true
			print("target changed")
			allOneLine = allOneLine .. value .. separator
			line = line .. value .. separator
			x = mainmemory.readbyte(addr+9)
			y = mainmemory.readbyte(addr+7)
		else
			line = line .. "" .. separator
		end
	end

	line = line .. x .. separator .. y	


	if targetAppeared == false then return nil end
	return line
end

while true do
	-- read_u16_le
	
	-- only record when screen state == 1
	screenState = mainmemory.readbyte(0x0216)

	if previousState == 1 and screenState ~= 1 then
		io.close(file)
		print("file saved!")
		return
	end

	if previousState ~= 1 and screenState == 1 then
		startFrameNumber = frameCount
	end

	previousState = screenState

	line = getRecordLine()
	if line ~= nil then
		io.write(line .. "\n")
	end	

	frameCount = frameCount + 1

	emu.frameadvance()
end