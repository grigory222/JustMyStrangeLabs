#!/bin/sh


echo -e "Подготовка..."
chmod -R +rwx lab0 
rm -rf lab0

mkdir lab0
cd lab0

echo -e "Создаю дерево каталогов и файлов с содержимым..."
mkdir glalie4
cd glalie4
  echo -e "Способности Blaze Landslide Oblivious\nSimple" > numel
  echo -e "Развитые способности Rattled\nMoxie" > mightyena
  mkdir palpitoad happiny axew
  cd ..
echo -e "weigth=212.3 height=59.0 atk=12\ndef=" > krookodile0
echo -e "Способности Meditate Confusion Detect Hidden Power" > medicham8
echo -e "Mind Reader Feint Calm Mind Force Palm Hi Jump Kick Psych Up" >> medicham8
echo -e "Acupressure Power Trick Reversal Recover" >> medicham8
echo -e "Живет  Beach\nFreshwater" > slowbro4
mkdir surskit0
cd surskit0
  mkdir excadrill
  echo -e "Развитые способности Shadow\nTag" > gothorita
  echo -e "Развитые способности Sand Force" > gastrodon
  echo -e "satk=8\nsdef=14 spd=7" > mantine
  mkdir amoonguss
  cd ..
mkdir turtwig2
cd turtwig2
  echo -e "Способности Poison Sting Screech Pursuit" > scolipede
  echo -e "Protect Poison Tail Bug Bite Venoshok Baton Pass Agility Steamroller" >> scolipede
  echo -e "Toxic Rock Climb Double-Edge" >> scolipede
  echo -e "Ходы Avalanche Bounce Dive" > wailmer
  echo -e "Hyper Voice Icy Wind Rollout Sleep Talk Snore Zen\nHeadbutt" >> wailmer
  mkdir lotad
  echo -e "weigth=131.4 height=51.0 atk=10 def=6" > shiftry
  echo -e "Ходы\nBug Bite Electroweb Giga Drain Grasswhistle Iron Defense Magic Coat" > swadloon
  echo -e "Razor Leaf Seed Bomb Signal Beam Sleep Talk Snore Synthesis Worry\nSeed" >> swadloon
  echo -e "Развитые способности Tangled Feet" > dodrio
  cd ..



echo -e "Установливаю права на файлы и каталоги..."
cd glalie4
  chmod u=---,g=rw-,o=-w- numel
  chmod 400 mightyena
  chmod uo=-wx,g=rwx palpitoad
  chmod u=r-x,go=rwx happiny
  chmod 755 axew
  cd ..
chmod u=r-x,g=rwx,o=rw- glalie4

chmod u=rw,go=- krookodile0
chmod u=r,go=- medicham8
chmod ug=-,go=rw slowbro4
chmod u=rwx,go=-wx surskit0

cd surskit0
  chmod 571 excadrill
  chmod 066 gothorita
  chmod u=r,go=- gastrodon
  chmod a=r mantine
  chmod u=r-x,g=rwx,o=rw- amoonguss
  cd .. 

cd turtwig2
  chmod 044 scolipede
  chmod u=rw,g=w,o=- wailmer
  chmod 511 lotad
  chmod ug=-,o=r shiftry
  chmod u=---,go=r-- swadloon
  chmod 440 dodrio
  cd ..
chmod 551 turtwig2




echo -e "Копирую часть дерева и создаю ссылки внутри дерева..."

# Выдать права
chmod u+w turtwig2
chmod u+wx glalie4

ln -s /home/studs/s408402/opd/lab0/medicham8 turtwig2/swadloonmedicham 

chmod u+r glalie4/numel
cat surskit0/gastrodon glalie4/numel > medicham8_62
chmod u-r glalie4/numel

ln slowbro4 turtwig2/shiftryslowbro

# скопировать рекурсивно директорию surskit0 в директорию lab0/surskit0/amoonguss
chmod u+r surskit0/gothorita
chmod u+wx surskit0/amoonguss
cp -r surskit0 tmp
cp -r tmp surskit0/amoonguss
rm -rf tmp
chmod u-r surskit0/gothorita
chmod u-wx surskit0/amoonguss
# ---------

cp krookodile0 glalie4/axew

cp medicham8 glalie4/mightyenamedicham

ln -s /home/studs/s408402/opd/lab0/surskit0/ ./Copy_49

chmod u-w turtwig2
chmod u-wx glalie4

#для отчета
#ls -lR


# ============ Поиск и фильтрация файлов, каталогов и данных ==============


# 1
echo -e "\nРазмеры файлов:"
wc --chars surskit0/gastrodon surskit0/mantine turtwig2/scolipede turtwig2/wailmer turtwig2/shiftry turtwig2/swadloon 2>/dev/null | sort -n -r | grep -v total

# 2
echo -e "\nЗаканчиваются на 4:"
ls -ltR 2>/dev/null | grep -v total | grep 4$ | tail -n 3

# 3
echo -e "\nСодержимое файлов в turtwig2/ :"
cd turtwig2
cat -n `ls` 2>/dev/null | grep -v Po
cd ..

# 4
echo -e "\nРазмеры файлов:"
#wc --chars surskit0/gastrodon surskit0/mantine turtwig2/scolipede turtwig2/wailmer 2>/tmp/error | sort -n | grep -v total
wc --chars surskit0/gastrodon surskit0/mantine turtwig2/scolipede turtwig2/wailmer | sort -n | grep -v total

# 5
echo -e "\nРазмеры файлов директории lab0:"
wc --chars m* */m* */*/m* 2>/tmp/errors408402 | sort -n -r | grep -v total

# 6
echo -e "\nТри первых элемента:" 
ls -lu . ./* ./*/* ./*/*/* 2>&1 | head -n 3




# ================== Удаление файлов и каталогов ====================
echo -e "\nУдаление файлов и каталогов..."

chmod u+w slowbro4
rm slowbro4

chmod u+w surskit0/mantine
rm surskit0/mantine

chmod u+w turtwig2/
chmod u+w turtwig2/shiftryslowbro
rm ./turtwig2/shiftryslowb*

rm -rf turtwig2

chmod u+w surskit0/amoonguss
rm -rf amoonguss

#для отчета
#ls -lR
