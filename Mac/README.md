
## Install Brew

from https://brew.sh/
```
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

## add space in dock

```
defaults write com.apple.dock persistent-apps -array-add '{"tile-type"="spacer-tile";}'; killall Dock
```

## Auto Completion

```
curl https://raw.githubusercontent.com/git/git/master/contrib/completion/git-completion.bash -o ~/.git-completion.bash
```
in .bash_profile : 
```
if [ -f ~/.git-completion.bash ]; then
  . ~/.git-completion.bash
fi
```

## Delete System App

- Restart and press Command R
- run this command on the terminal:
```
csrutil disable
# remove the app here
csrutil enable
```
		
