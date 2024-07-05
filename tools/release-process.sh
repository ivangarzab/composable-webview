#!/bin/bash
# The purpose of this script is to execute a full release process at once.

if [ -z "$1" ]; then
  echo "Error: Missing version number parameter."
  exit 1
fi
VERSION_NAME="$1"


# Step 1: Create release branch
echo "Creating release branch for upcoming release v$VERSION_NAME"
git checkout master
git fetch
git status
git checkout -b release/"$VERSION_NAME"

# Step 2: Update library version number
echo "Updating library version to $VERSION_NAME"
chmod +x tools/update-version.sh
./tools/update-version.sh "$VERSION_NAME"
git commit -am "Update library version for release v$VERSION_NAME"

# Step 3:
echo "Pushing release branch into 'origin' and updating 'master' branch"
# Publish release branch into 'origin'
BRANCH_NAME=release/"$VERSION_NAME"
git push -u origin "$BRANCH_NAME"
# Merge release branch into 'master'
git checkout master
git merge --no-ff "$BRANCH_NAME"
DATE_TODAY='date +"%Y-%m-%d"'
# Tag the release
git tag -a "$VERSION_NAME" -m "Tagging release v$VERSION_NAME on $DATE_TODAY"

# Step 4
echo "Pushing all changes up to the 'origin'"
# Push up 'master' branch
git push origin master
# Push up new tag
git push origin "$VERSION_NAME"
echo "All changes have been pushed to origin"