
## Install Brew

```
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
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
		
