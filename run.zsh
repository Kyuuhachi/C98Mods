#!/usr/bin/env zsh

SCRIPT='
import json
import os.path
import itertools
import sys

errors = json.loads(input())
maxl = 0
haserr = False
for e in errors:
	e["color"] = 3 if e["warning"] else 1
	maxl = max(maxl, len(str(e["line"])))
	haserr |= not e["warning"]

for path, errors2 in itertools.groupby(errors, lambda l: l["filename"]):
	errors2 = list(errors2)
	print("\x1B[35;1m{file}\x1B[m".format(file=os.path.relpath(path, "./src/main/java")))
	for e in errors2:
		print(" \x1B[3{color};1m{line:{maxl}}\x1B[m: {message}".format(maxl=maxl, **e))

sys.exit(haserr)
'

# command -v eclim >/dev/null && {
# 	eclim -command problems -p C98Mods | python3 -c $SCRIPT || {
# 		echo "Errors found; quitting"
# 		exit
# 	}
# }

jq -r '.authenticationDatabase[.selectedUser] | .displayName, .uuid' ~/.minecraft/launcher_profiles.json | { read username; read uuid }

classpath="$(mvn -q dependency:build-classpath -DincludeScope=runtime -Dmdep.outputFile=/dev/stdout)"
[[ -d mc ]] || mkdir mc
cd mc
echo "Running..."
java \
	-classpath ../target/classes:"$classpath" \
	-Djava.library.path=../target/natives \
	net.minecraft.launchwrapper.Launch \
	--version C98Mods \
	--tweakClass c98.core.main.Main \
	--username "$username" \
	--uuid "$uuid" \
	--accessToken 0 \
	--userProperties {}
