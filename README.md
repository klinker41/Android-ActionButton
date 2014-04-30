# ActionButton

![Android-ActionButton](https://raw.githubusercontent.com/klinker41/Android-ActionButton/master/preview.png)

### Information

[Video](https://www.youtube.com/watch?v=AU8RalwVOXU)

Implementing an ActionButton in your Android apps is extremely simple, it works very similarly to creating a toast
or AlertDialog.

Here is a sample to show a button:
``` java
final ActionButton button = new ActionButton(mContext);
button.setImageResource(R.drawable.icon);
button.setColors(getResources().getColor(android.R.color.holo_blue_light),
    getResources.getColor(android.R.color.holo_blue_dark));
button.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v) {
        // do something with click
        button.hide();
    }
});
button.show();
```

Note: Currently only supported for API version 14 and higher, if I have time I may be able to backport it to lower, but
I highly suggest people begin setting their minSdkVersion to 14, there are so many more possibilities and Gingerbread
is dying!

## License

    Copyright 2013-2014 Jacob Klinker

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.