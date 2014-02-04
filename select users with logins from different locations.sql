SELECT DISTINCT
   wu.username, ul1.id, ul1.login_timestamp, ul1.ip_address,
   ul1.continent_name, ul1.country_name, ul1.registered_country_name, ul1.represented_country_name, 
   ul1.city_name, ul1.postal_code, ul1.location_time_zone, ul1.most_specific_subdivision_name, ul1.traits_ip_address 
FROM 
  public.wiki_user wu, public.user_login ul1, public.user_login ul2
WHERE wu.username  = ul1.username
AND   wu.username  = ul2.username
AND   ul1.username = ul2.username
AND   (   ul1.continent_name 			!= ul2.continent_name 
       OR ul1.country_name 			!= ul2.country_name  
       OR ul1.registered_country_name 		!= ul2.registered_country_name 
       OR ul1.represented_country_name 		!= ul2.represented_country_name 
       OR ul1.city_name 			!= ul2.city_name 
       OR ul1.postal_code 			!= ul2.postal_code 
       OR ul1.location_time_zone 		!= ul2.location_time_zone 
       OR ul1.most_specific_subdivision_name 	!= ul2.most_specific_subdivision_name 
       OR ul1.traits_ip_address 		!= ul2.traits_ip_address
      );
