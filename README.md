# Watr.li Dashboard

## Development

### SCSS compilation

Since the sbt plugins for SASS compilation are really slow (non-incremental?) we've resorted to compiling SASS using the compass gem. So when developing, run

    $ compass watch

in the project's root directory.