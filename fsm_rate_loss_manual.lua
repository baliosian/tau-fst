	-- function action_kr(e)
actions.action_kr = function(e)
	local levels = getDomain('rate')
	local retMax = -100
	local l
	for _,lr in ipairs(levels) do
		local ret = functions.action_kr(lr)
		if ret > retMax then
			retMax = ret
			l = lr
		end
	end
	return notifs.change_rate(l,e)
end
	-- Membership function for action_kr
functions.action_kr = function(l)
	------------------------------------------------
	-- TODO: Complete this with your function code. --
	------------------------------------------------
end

	-- function action_ip(e)
actions.action_ip = function(e)
	local levels = getDomain('power')
	local retMax = -100
	local l
	for _,lp in ipairs(levels) do
		local ret = functions.action_ip(lp)
		if ret > retMax then
			retMax = ret
			l = lp
		end
	end
	return notifs.change_power(l,e)
end

	-- Membership function for action_ip
functions.action_ip = function(l)
	------------------------------------------------
	-- TODO: Complete this with your function code. --
	------------------------------------------------
end

	-- function action_ir(e)
actions.action_ir = function(e)
	local levels = getDomain('rate')
	local retMax = -100
	local l
	for _,lr in ipairs(levels) do
		local ret = functions.action_ir(lr)
		if ret > retMax then
			retMax = ret
			l = lr
		end
	end
	return notifs.change_rate(l,e)
end
	-- Membership function for action_ir
functions.action_ir = function(l)
	------------------------------------------------
	-- TODO: Complete this with your function code. --
	------------------------------------------------
end

	-- function action_dr(e)
actions.action_dr = function(e)
	local levels = getDomain('rate')
	local retMax = -100
	local l
	for _,lr in ipairs(levels) do
		local ret = functions.action_dr(lr)
		if ret > retMax then
			retMax = ret
			l = lr
		end
	end
	return notifs.change_rate(l,e)
end
	-- Membership function for action_dr
functions.action_dr = function(l)
	------------------------------------------------
	-- TODO: Complete this with your function code. --
	------------------------------------------------
end

	-- function action_kp(e)
actions.action_kp = function(e)
	local levels = getDomain('power')
	local retMax = -100
	local l
	for _,lp in ipairs(levels) do
		local ret = functions.action_kp(lp)
		if ret > retMax then
			retMax = ret
			l = lp
		end
	end
	return notifs.change_power(l,e)
end

	-- Membership function for action_kp
functions.action_kp = function(l)
	------------------------------------------------
	-- TODO: Complete this with your function code. --
	------------------------------------------------
end

	-- function event_ll(e)
events.event_ll = function(e) 
	shared["incomming_event"] = e
	-----------------------------------------------
	-- TODO: Complete this with your event code. --
	-----------------------------------------------
end

	-- function event_hp(e)
events.event_hp = function(e) 
	shared["incomming_event"] = e
	-----------------------------------------------
	-- TODO: Complete this with your event code. --
	-----------------------------------------------
end

	-- function event_hl(e)
events.event_hl = function(e) 
	shared["incomming_event"] = e
	-----------------------------------------------
	-- TODO: Complete this with your event code. --
	-----------------------------------------------
end

	-- function event_lp(e)
events.event_lp = function(e) 
	shared["incomming_event"] = e
	-----------------------------------------------
	-- TODO: Complete this with your event code. --
	-----------------------------------------------
end

	-- function event_hr(e)
events.event_hr = function(e) 
	shared["incomming_event"] = e
	-----------------------------------------------
	-- TODO: Complete this with your event code. --
	-----------------------------------------------
end

	-- function event_lr(e)
events.event_lr = function(e) 
	shared["incomming_event"] = e
	-----------------------------------------------
	-- TODO: Complete this with your event code. --
	-----------------------------------------------
end

