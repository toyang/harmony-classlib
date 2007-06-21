#include "TypeDefinition.h"

fint fwcslen(fwchar_t* str)
{
    if (!str)
        return 0;
        
	fint counter=0;
	fwchar_t* tmpstr = str;
	while(*tmpstr != 0)
	{
		tmpstr++;
		counter++;
	}
	return counter;
}

fint fwcscmp(fwchar_t* str1, fwchar_t* str2)
{
    if (str1 == 0 || str2 == 0)
        return str1 - str2;
        
	fwchar_t* tmpstr1 = str1; 
	fwchar_t* tmpstr2 = str2;
	while(*tmpstr1 != 0 || *tmpstr2 !=0)
	{
        if (*tmpstr1 != *tmpstr2)
			return -1;

		tmpstr1++;
		tmpstr2++;
	}

	return 0;
}