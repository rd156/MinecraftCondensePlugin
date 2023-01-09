# MinecraftCondensePlugin

```diff
- The condense command allows you to transform items into blocks.
```

## The associated permission is condense.use

### Configuration:

The configuration file is in the "Condense" folder in the plugins part of your server. </br>
In it you can activate the display of a message for each transformation. (Display->List)</br>
```yml
display:
  list: true
```

### List of item that you can condense:
You can add every element that you want change with condense command. If you remove the block of one condense this items can not be condense with this command.</br>
```yml
condense:
  IRON_NUGGET:
    output: IRON_INGOT
    ratio_in: 9
    ratio_out: 1
  GOLD_NUGGET:
    output: GOLD_INGOT
    ratio_in: 9
    ratio_out: 1
```
You can also customize the set of messages displayed by the plugin.</br>
By default the messages displayed is in english but you can change this message in the configuration file.</br>
The messages are divided into several categories:</br>
The error messages found in the part error</br>
The status messages found in the part condense</br>
```yml
message:
  condense:
    resume: "§a[number] items have been transformed."
    item: "§a[item1] change into [item2]."
  error:
    inventory_full: "§4Inventory full: impossible to change [item1] into [item2]."
    ratio_zero: "§4Attention the conversion ratio of [item1] is equal to 0."
```
