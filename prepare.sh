ln -snf /usr/share/zoneinfo/Europe/Minsk /etc/localtime && echo Europe/Minsk > /etc/timezone
sudo apt-get install -y tzdata
sudo apt-get update -y
sudo apt-get install -y apt-utils
#java
sudo apt install -y openjdk-11-jdk
#maven
sudo apt install -y maven