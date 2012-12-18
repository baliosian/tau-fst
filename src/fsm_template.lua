print ("FSM loading...")

local function FSM(t)
	local a = {}
	for _,v in ipairs(t) do
		local old, t_function, new, action = v[1], v[2], v[3], v[4]
			if a[old] == nil then a[old] = {} end
			table.insert(a[old],{new = new, action = action, t_function = t_function})
	  	end
  	return a
end


--auxiliar functions to be used when detecting happening events
local function register_as_happening(event)
	table.insert(happening_events, event)
end
local function unregister_as_happening(filter)
	for i, event in ipairs(happening_events) do
		local matches = true
		for key, value in pairs(filter) do
			if not event[key]==value then
				matches=false
				break
			end
		end
		if matches then
			--print("%%%%%%%unregistering ", i)
			table.remove(happening_events, i)
		end
	end	
end
-- Tabla para compartir el evento con la acción.


local shared = {}

--begin generated code
--------------------------------------------------------------------------

--datahere--

--------------------------------------------------------------------------
--end generated code

local current_state=init_state --current state
local i_event=1 --current event in window

function initialize()
 	print("FSM: initializing")
	return initialization_subs or {}
end

--advances the machine a single step.
--returns nil if arrives at the end the window, or the event is not recognized
--otherwise, returns the resulting list from the action
local function fst_step()
	local event_reg = window[i_event]
	if not event_reg then return end --window finished
	local event=event_reg.event
			
	local state=fsm[current_state]
	--search first transition that verifies e
	local best_tf=-2
	local transition
	for _, l in ipairs(state) do
		if l.t_function then
			local tf=l.t_function(event)
			if best_tf<tf then
				best_tf=tf
				transition=l
			end
		end
	end 
	if not transition or best_tf<0 then --last event wasn't recongized 
		current_state=nil
		return nil
	end 
	
	local ret_call
	local action=transition.action
	if action then ret_call=action(event) end
	i_event=i_event+1
	current_state = transition.new

	return ret_call	or {}
end

local function fst_traverse_epsilon()
	local ret={}
	local state=fsm[current_state]
	if not state then return ret end
	local transition=state[1] --asumes only 1 transition when epsilon
	while transition and transition.t_function==nil do
		local action=transition.action
		if action then table.insert(ret, action()) end
		current_state = transition.new
		state=fsm[current_state]
		transition=state[1] --asumes only 1 transition when epsilon
	end
	if not transition then --no outgoing transitions
		current_state=nil	
	end
	return ret
end

local function dump_window()
	local s="=> "
	for _,e in ipairs(window) do
		if e.event.message_type=="trap" then
			s=s .. tostring(e.event.watcher_id) ..","
		else
			s=s .. "#,"
		end
	end
	return s
end

local function insert_from_epsilon_trans(ret)
	local rets_eps=fst_traverse_epsilon()
	--queue generated actions
	for _, ret_eps in ipairs(rets_eps) do
		for _, r in ipairs(ret_eps) do
			table.insert(ret, r)
		end
	end		
end

function proccess_window_add()
	print("FSM: WINDOW ADD ", table.maxn(window), dump_window())
	
	local ret = {}
	if current_state then --and fsm[current_state] then
		local ret_call=fst_step()
		if ret_call then 
			--queue generated actions
			for _, r in ipairs(ret_call) do
				table.insert(ret, r)
			end		
		end

		insert_from_epsilon_trans(ret)		
	end
	if table.maxn(ret)>0 then
		print ("FSM: WINDOW ADD generating output ", table.maxn(ret), current_state)
	end
	return ret
end

function proccess_window_move()
	print("FSM: WINDOW MOVE", table.maxn(window), dump_window())

	i_event, current_state = 1, init_state
	local ret={}

	insert_from_epsilon_trans(ret)

	while current_state do --and fsm[current_state] do
		local ret_call=fst_step()
		if not ret_call then break end --window finished or chain not recognized
		if ret_call then 
			--queue generated actions
			for _, r in ipairs(ret_call) do
				table.insert(ret, r)
			end
		end

		insert_from_epsilon_trans(ret)
	end
	if table.maxn(ret)>0 then
		print ("FSM: WINDOW MOVE generating output ", table.maxn(ret), current_state)
	end
	return ret
end

print ("FSM loaded.")



