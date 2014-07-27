# Apache2 VHost Config
<VirtualHost *:80>
	ServerAdmin postmaster@<#if host_name?starts_with("www.")>${host_name?substring(4)}<#else>${host_name}</#if>
	ServerName ${host_name}
	<#if host_alias != ''>ServerAlias ${host_alias}</#if>
	DocumentRoot ${website_root_path}
	DirectoryIndex index.html index.jsp

	JkMount /* ${service_name}
	
	<Directory ${website_root_path}>
  	Options -Indexes FollowSymLinks
  	AllowOverride None
  	Order allow,deny
  	Allow from all
	</Directory>
</VirtualHost>

# JK AJP1.3 Config
worker.list=${service_name}
worker.${service_name}.port=${ajp_port}
worker.${service_name}.host=${ajp_host}
worker.${service_name}.type=ajp13
worker.${service_name}.lbfactor=1