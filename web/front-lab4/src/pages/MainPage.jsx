import {MyPaperBox} from "../components/AuthForm.jsx";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import {InputPointForm} from "../components/InputPointForm.jsx";
import {ResultsDataGrid} from "../components/ResultsDataGrid.jsx";
import {useGetPokemonByNameQuery, useSendPointMutation} from "../api/myLegendaryApi.js";


export function MainPage() {

    const [sendPoint, response] = useSendPointMutation();

    const formSubmitHandler = async (e) => {
        e.preventDefault();
        try {
            alert('here2');
            await sendPoint({ x:1, y:1, r:1 }).unwrap();
            alert("success");
            console.log(response);
        } catch (err) {
            console.error(err);
            alert("err");
        }
    };

    return (
        <Box container
             sx={{
                 display: 'flex',
                 flexDirection: 'column',
                 alignItems: 'center',
                 justifyContent: 'center',
                 boxShadow: 4,
                 borderRadius: 10,
                 mx: 10,
                 my: 5,
             }}
        >
            <Typography sx={{
                display: 'flex',
                flexDirection: 'row',
                justifyContent: 'center',
                my: 4,
                fontSize: 40,
            }}>Добро пожаловать</Typography>

            <Box
                sx={{
                    display: "flex",
                    flexDirection: 'row',
                    justifyContent: "center",
                    flexWrap: 'wrap',
                    width: '100%',

                }}
            >
                <MyPaperBox
                    id="graph-box"
                    sx={{
                        width: {md: 1 / 3, xs: '66%'},
                        mx: 5,
                        my: 5,
                        borderRadius: 5,
                        boxShadow: 1
                    }}
                >
                    {/* Содержимое для graph-box */}
                </MyPaperBox>

                <MyPaperBox
                    id="form-box"
                    onSubmit={formSubmitHandler}
                    sx={{
                        width: {md: 1 / 3, xs: '66%'},
                        mx: 5,
                        my: 5,
                        borderRadius: 5,
                        boxShadow: 1
                    }}
                >
                    <InputPointForm/>
                </MyPaperBox>
            </Box>
            <div style={{width: '100%'}}>
                <ResultsDataGrid/>
            </div>

        </Box>
    )
}

