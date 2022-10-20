update sustc.public.handle set time = current_date where time is null;

update sustc.public.handle set courier_id = 1 where courier_id is null;

delete from sustc.public.handle where record_id in
                         (select id from sustc.public.record where item_class = 'coconut');

delete from sustc.public.transit where record_id in
                          (select id from sustc.public.record where item_class = 'coconut');

delete from sustc.public.record where item_class = 'coconut'