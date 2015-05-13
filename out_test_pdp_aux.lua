print ("FSM loading...")

local function FSM(t)
	local a = {}
	for _,v in ipairs(t) do
		local old, t_function, new, actions = v[1], v[2], v[3], v[4]
    
    if a[old] == nil then a[old] = {} end
    if new then
      table.insert(a[old],{new = new, actions = actions, t_function = t_function})
    end    
  end
  return a
end


--auxiliar functions to be used when detecting happening events
local function register_as_happening(event)
  happening_events[event]=true
end
local function unregister_as_happening(event)
  happening_events[event]=nil
end
local function unregister_as_happening_f(filter)
	for event, _ in pairs(happening_events) do
		local matches = true
		for key, value in pairs(filter) do
			if not event[key]==value then
				matches=false
				break
			end
		end
		if matches then
			happening_events[event]=nil
		end
	end	
end


local shared = {}

--begin generated code
--------------------------------------------------------------------------


--initialization
local initialization_notifs = {
}
local initialization_subs = {
}

-------------------------------

-- Non-Generated functions ----

-- Obtained from:          ----

-- out_test_pdp_aux_manual.lua  --

-------------------------------
	-- function event_D(e)
actions.event_D = function(e)
	e = shared["incomming_event"]
	------------------------------------------------
	-- TODO: Complete this with your action code. --
	------------------------------------------------
end

	-- function action_C(e)
actions.action_C = function(e)
	e = shared["incomming_event"]
	------------------------------------------------
	-- TODO: Complete this with your action code. --
	------------------------------------------------
end


-------------------------------

-- End of functions        ----

-------------------------------

local init_state="22"
--shares
local shared_0 = {}

--events
-----------------------------------------------
--          BEGIN COMPOSITE EVENTS           --
-----------------------------------------------
-- ¬D
events.f0 = function(e) 
	local nonneg = events.event_D(e)
	return 1-nonneg
end
-----------------------------------------------
--           END COMPOSITE EVENTS            --
-----------------------------------------------

--actions
-----------------------------------------------
--          BEGIN COMPOSITE ACTIONS          --
-----------------------------------------------
-----------------------------------------------
--           END COMPOSITE ACTIONS           --
-----------------------------------------------

--transitions
local fsm = FSM{
  {"22", events.event_D, "21", {actions.action_C, }},
  {"22", events.f0, "22", {actions.event_D, actions.action_C, }},
}

--final states
local is_accept =   {
['21']=true,
}


--------------------------------------------------------------------------
--end generated code

local current_state=init_state --current state
local i_event=1 --current event in window

function initialize()
 	print("FSM: initializing")
	return initialization_subs or {}, initialization_notifs or {}
end

local function dump_window()
	local s="=> "
	for _,e in ipairs(window) do
		if e.event.message_type=="trap" then
			s=s .. tostring(e.event.mib) ..","
		else
			s=s .. "#,"
		end
	end
	return s
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

--advances the machine a single step.
--returns nil if arrives at the end the window, or the event is not recognized
--otherwise, returns the resulting list from the action
local function fst_step()
	local event_reg = window[i_event]
	if not event_reg then return end --window finished
	local event=event_reg.event
			
	local state=fsm[current_state]
  assert(#state>0)
	--search first transition that verifies e
	local best_tf=-1
	local transition
	for _, l in ipairs(state) do
    local tf=l.t_function(event)
    if best_tf<tf then
      best_tf=tf
      transition=l
    end
	end 
  assert(transition)
	
	local ret_call = {}
	if transition.actions then
    for _, action in ipairs(transition.actions) do
      local ret_action = action(event)
      for _, v in ipairs(ret_action) do ret_call[#ret_call+1] = v end
    end
  end
  
	i_event=i_event+1
	current_state = transition.new
<<<<<<< HEAD
	print ("NEW STATE:", current_state, "#TRANS:", #fsm[current_state],  "#RETS:", #ret_call, "ACCEPT:", is_accept[current_state], "FINAL:", #fsm[current_state]==0)
  return ret_call, is_accept[current_state], #fsm[current_state]==0
=======
  
  return ret_call	or {}, is_accept(current_state), #fsm[current_state]==0
>>>>>>> master
end

function step()
	print("FSM: WINDOW STEP ", #window, dump_window())
	
	local ret, accept, final = {}, false, false
  
  repeat
<<<<<<< HEAD
    local ret_step
		ret_step, accept, final = fst_step()
		if ret_step then 
			for _, r in ipairs(ret_step) do ret[#ret+1]=r	end --queue generated actions
		end
  until accept or i_event>#window
=======
		local ret_step, accept, final = fst_step()
		if ret_step then 
			for _, r in ipairs(ret_step) do ret[#ret+1]=r	end --queue generated actions
		end
  until accept or i_event==#window
>>>>>>> master
  assert(not (final and not accept))
  
  if accept then
    --purge consumed events from window
<<<<<<< HEAD
    print("Purge consumed events", #window)
=======
>>>>>>> master
    local i=1
    local e = window[i_event]
    repeat
      if happening_events[window[i]] then
        i=i+1
      else
<<<<<<< HEAD
        table.remove(window, i)
        i_event=i_event-1
      end
    until window[i]==e
    if not happening_events[window[i]] then table.remove(window, i) end
    print("Purge consumed events", #window)
=======
        table.remove[window, i]
        i_event=i_event-1
      end
    until window[i]==e
    if not happening_events[window[i]] then table.remove[window, i] end
>>>>>>> master
  end
  
	if #ret>0 then
		print ("FSM: WINDOW STEP generating output ", #ret, accept, final, current_state)
	end
	return ret, accept, final
end

function reset()
  current_state=init_state 
  i_event=1 
  happening_events={}
<<<<<<< HEAD
  print ("FSM: RESET")
=======
>>>>>>> master
end

print ("FSM loaded.")

