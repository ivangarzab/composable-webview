#!/bin/bash
# The purpose of this script is to automatically update the app's build.gradle.kts versionName,
# given the parameter provided, as well as incrementing the versionCode up by 1 for a release.

if [ -z "$1" ]; then
  echo "Error: Missing version number parameter."
  exit 1
fi
VERSION_NAME="$1"
SED_OPTION=
if [[ "$OSTYPE" == "darwin"* ]]; then
  SED_OPTION="-i ''"
else
  SED_OPTION="-i"
fi


# Update library version
VERSION_NAME_PATTERN="version"
echo "Updating the app versionName to: $VERSION_NAME"
sed "$SED_OPTION" "/$VERSION_NAME_PATTERN/ s/\=.*\"$/\= \"$VERSION_NAME\"/" webview/build.gradle.kts
echo "Library version has been updated to: $VERSION_NAME"