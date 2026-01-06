<style>
p:has(+ ul) {
  margin-bottom: 0;
}
p + ul {
  margin-top: 0;
}
</style>

> [!NOTE]
> **This plugin is designed to work in conjunction with [Luck Perms](https://luckperms.net) but will still work without, Just with less features ([*Using Without Luck Perms*](#using-without-luck-perms))**.

# Main Features

- Swaps perms around using luck perms *(Reqiures [Luck Perms](https://luckperms.net))*
- Vanish and hide form players
- 2 inventories, Defualt / Admin
- Admin chat to message all admin players
- Help chat for normal players to request help from admins
- Set a joinmode and join in differnt modes **(Including vanish)**

<br>

# Admin Modes

### **None**: defualt mode, effectively does nothing
  - Puts player into Survival.
  - Swaps inventory to Defualt inventory.

### **Admin**: Gives Perms
  - Puts player into Creative.
  - Swaps inventory to Admin inventory.

### **Vanish**: Gives Perms, hides player from all other players without perms
  - Puts player into Spectator.
  - Swaps inventory to Admin inventory.

<br>

# Commands

## **/admin**: Switches to admin mode, unless in admin mode then switches to normal mode
**Aliases**:  
  - /a
  
**Defualt Permission**: op

**Related Permissions**:
  - AdminMode.Admin.Admin

## **/vanish**: Switches to admin mode, unless in admin mode then switches to normal mode
**Aliases**: 
  - /v
  
**Defualt Permission**: op

**Related Permissions**:
  - AdminMode.Admin.Vanish
  - AdminMode.See.Vanish

## **/adminChat**: Sends a message to all other players in admin mode
**Aliases**: 
  - /ac

**Defualt Permission**: op

**Related Permissions**:
  - AdminMode.Admin.Chat
  - AdminMode.See.Chat

## **/joinMode**: Sets the admin mode to set apon joining the server
**Aliases**: 
  - /jm
**Defualt Permission**: op

**Related Permissions**:
  - AdminMode.Admin.JoinMode

## **/requestHelp**: For normal players: sends a message to admins asking for help
**Aliases**: 
  - /rq 
  - /messageAdmin 
  - /ma
  
**Defualt Permission**: all

**Related Permissions**:
  - AdminMode.HelpChat.Message
  - AdminMode.HelpChat.See

<br>

# Config
  - ### **enabled**
    - **Defualt Value**: true
    - **Type**: Boolean
    - **Description**: Enables / Disables the plugin

  <br>

  > [!NOTE]
  > **Only works if luck perms is installed on the server**
  - ### **luck-perms-integration**
    - **Description**: Group for more config
    - **Children**
      - **enabled**
        - **Defualt Value**: true
        - **Type**: Boolean
        - **Description**: Enables / Disables luck perms intergration
      - **createGroups**
        - **Defualt Value**: false
        - **Type**: Boolean
        - **Description**: If the plugin should create luck perms groups on startup
      - **adminGroup**
        - **Defualt Value**: "admin"
        - **Type**: String
        - **Description**: Name of the group (will have the prefix "[Admin]")

<br>

# Using Without Luck Perms

If you are **not** using luck perms you'll need to give players op top use the features. \
Even though one of the main intents of this plugin is to not give people op but allow them to gain perms when needed. \
This plugin still can be useful for inventory and game mode swapping along with vanish.

<br>

# Technical Info

When using Luck Perms when switching to Admin / Vanish mode the plugin will give you the perms group, and when switching to None mode it will remove the perm group.

By default the perm group has all permisions, **It is highly recomended that you change this**.

The Admin group will have the prefix [Admin] attached to it along with perms to use all commands in this plugin.

<br>

# Issues

If you encounter any bugs or would like to request a feature, make an issue on gitbub

<br>

# Licence

You can use this plugin on any server as long as you **are not** directly profiting off the plugin