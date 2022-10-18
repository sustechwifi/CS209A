
--Advanced Query 1: Figure out which containers have serviced for several years depending on
--  type.
select code,container.type,(current_date - transit.time) / 365 as served
from container,record,transit
where container.id = record.container_id
and record.id = transit.record_id
and transit.type = 3 and transit.time is not null
and ((current_date - transit.time) / 365 >= 5 and container.type = 'Dry Container'
    or (current_date - transit.time) / 365 >= 4 and container.type = 'Flat Rack Container'
    or (current_date - transit.time) / 365 >= 3 and container.type = 'Open Top Container'
    or (current_date - transit.time) / 365 >= 2 and container.type = 'Reefer Container'
    or (current_date - transit.time) / 365 >= 1 and container.type = 'ISO Tank Container');

--Advanced Query 2: Figure out who handled the most items grouping by company and city.
select company,city,courier,tel,cnt
from(
    select company.name company,city.name city,courier.name courier,
        courier.phone_number tel,count(*) cnt,
        row_number() over (partition by company.name,city.name order by count(*) desc) rank
    from city,company,courier,handle
    where courier.city_id = city.id
        and courier.company_id = company.id
        and handle.courier_id = courier.id
    group by company,city,courier.name,courier.phone_number) t1
where rank = 1
order by company,cnt desc;

--Advanced Query 3: Figure out which city has the cheapest export tax for each item_class
select Class,Name,ExportRate from(
    select distinct item_class Class,city.name Name,
    concat(round(100.0 * tax / item_price,2),'%')
    as ExportRate, row_number() over (PARTITION BY item_class order by tax / item_price) rank
    from record,city,transit
    where transit.type = 2
      and transit.city_id = city.id
      and transit.record_id = record.id
      and tax is not null
    order by ExportRate, rank) t1
where rank = 1;
