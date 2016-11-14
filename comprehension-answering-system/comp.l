%{
	#include "y.tab.h"
	int extern yylval;
%}
%%
"who"|"where"|"when"|"why"|"what"|"which"	{return QWORD;}
"am"|"is"|"are"|"was"|"were"|"did"|"does"|"do"|"has"|"had"|"have" {return VERB;}
"?"	{return QMARK;}
[a-z][a-zA-Z0-9]*	{
				return ID;}
[ \t]	{return SP;}
"\n"	{return 0;}
%%

