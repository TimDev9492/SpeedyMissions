#!/bin/bash

key_file="/home/tim/.ssh/strato_nopasswd"
host="root@strato"
destination="/root/minecraft/devServer/plugins/"
screen_session_name="minecraft-server-devServer"

# $1 - the path to the jar file
if [ -z "$1" ]; then echo "No jar file provided!"; exit 1; fi

# check if file exists
if ! [ -f "$1" ]; then echo "File \"$1\" does not exist!"; exit 1; fi

# upload the file
sftp -i "$key_file" "$host" <<EOF
put "$1" "$destination"
EOF

# reload the server
echo "Reloading server..."
ssh -i "$key_file" "$host" "screen -S \"$screen_session_name\" -p 0 -X stuff \"reload confirm\"\`echo -ne '\015'\`"
if [ $? -eq 1 ]; then echo "Failed to reload the server."; exit 0; fi
echo "Success!"; exit 0
