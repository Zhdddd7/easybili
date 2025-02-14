# EasyBili
### File Structure
* po: persistent object, should be consistent with database tables
* mapper: access the database
* query: seal the query from frontend to manage complex queries
* vo: Object interact with frontend
* service: transaction logic
* controller: handle HTTP requests

### current bugs
1. clean redis token after refresh token
2. currently not support making playlist
3. extensions: AOP on type(currently on method)
4. new everyday login gains a coin


### TO Launch
1. start Mysql
2. start Redis
3. start ElasticSearch
4. start Frontend
5. start Backend


### command to launch

#打包前端
npm run build
#前端的配置
cd /www/server/panel/vhost/nginx
#nginx执行位置
cd /www/server/nginx/sbin
./nginx -s reload

nohup java -jar [name-of-jar] --server.port=7071 > /dev/null 2>&1 &

#Linux命令行执行以下命令进行下载
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.12.1-linux-x86_64.tar.gz

#解压压缩包在 /usr/local 目录命令
tar -zxvf elasticsearch-7.12.1-linux-x86_64.tar.gz -C /usr/local

#root用户不能直接启动Elasticsearch，所以需要创建一个专用用户，来启动ES：
useradd user-es

#创建所属组
chown user-es:user-es -R /usr/local/elasticsearch-7.12.1

#修改数据和日志的目录
#数据目录位置
path.data: /home/user-es/elasticsearch/data
#日志目录位置
path.logs: /home/user-es/elasticsearch/logs

#切换到user-es用户
su user-es

#进入bin目录，启动elasticsearch：
cd /usr/local/elasticsearch-7.12.1/bin

nohup ./elasticsearch > elasticsearch.log 2>&1 &

curl http://127.0.0.1:9200

#安装ik


#打包jar
mvn clean install -U

#查看当前的端口运行情况
sudo lsof -i :7071
sudo lsof -i :7070
#进入jar的执行目录

#挂起java
nohup java -jar easybili-admin-1.0.jar --server.port=7070 > /dev/null 2>&1 &
nohup java -jar easybili-web-1.0.jar --server.port=7071 > /dev/null 2>&1 &

#端口目录
服务端 -> 8011 -> 7071
后台 -> 8010 -> 7070


