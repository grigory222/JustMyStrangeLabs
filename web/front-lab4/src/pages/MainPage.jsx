import {MyPaperBox} from "../components/AuthForm.jsx";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import {InputPointForm} from "../components/InputPointForm.jsx";
import {ResultsDataGrid} from "../components/ResultsDataGrid.jsx";
import {useGetPointsQuery, useSendPointMutation} from "../api/myLegendaryApi.js";
import {useDispatch, useSelector} from "react-redux";
import {addResult, clearResults} from "../storage/ResultsSlice.js";
import {useEffect, useState} from "react";
import {Alert} from "@mui/material";


export function MainPage() {

    const [sendPoint] = useSendPointMutation();
    const rows = useSelector(state => state.reducer.results.array);
    const dispatch = useDispatch();

    const [formData, setFormData] = useState({x: null, y: '', r: null});
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [alert, setAlert] = useState(false);
    const [alertContent, setAlertContent] = useState('');

    const isLoggedIn = useSelector(state => state.reducer.auth.isLogged);

    const {data, error, isLoading, refetch} = useGetPointsQuery(); // RTK Query для получения данных


    // Загружаем данные в Redux при успешном ответе API
    useEffect(() => {
        console.log("use efffect");
        console.log();
        refetch();
        if (isLoggedIn && !isLoading) {
            dispatch(clearResults())
            if (data) {
                data.forEach((item, index) => {
                    dispatch(addResult({id: index + 1, ...item}));
                });
            }
        }
    }, [isLoggedIn, isLoading, data, dispatch]);

    const getLastRowId = () => {
        return rows.length > 0 ? rows[rows.length - 1].id : 0;
    };

    const formSubmitHandler = async (e) => {
        e.preventDefault();

        if (isSubmitting) return; // Предотвращаем повторное нажатие
        setIsSubmitting(true);

        const {x, y, r} = formData;
        if (x === null || r === null || y === '') {
            setAlert(true);
            setAlertContent("Должны быть заполнены все поля")
            setIsSubmitting(false);
            return;
        }
        if (Number(y) < -5 || Number(y) > 3) {
            setErrorMessage("Значение Y должно быть в диапазоне [-5;3]")
            setIsSubmitting(false);
            return;
        }

        try {
            const response = await sendPoint({x, y: parseFloat(y), r}).unwrap()
            const id = Number(getLastRowId()) + 1;
            dispatch(addResult({id, x, y: parseFloat(y), r, result: response.result}));
            console.log('fulfilled: ' + JSON.stringify(response));
        } catch (err) {
            setAlert(true);
            setAlertContent(err.message);
            console.log(err);
        }

        setAlert(false);
        setIsSubmitting(false); // Разблокировать кнопку после завершения
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
                    sx={{
                        width: {md: 1 / 3, xs: '66%'},
                        mx: 5,
                        my: 5,
                        borderRadius: 5,
                        boxShadow: 1
                    }}
                >
                    <InputPointForm
                        formData={formData}
                        setFormData={setFormData}
                        formSubmitHandler={formSubmitHandler}
                        errorMessage={errorMessage}
                    />
                </MyPaperBox>
            </Box>
            {alert ? <Alert severity='error'>{alertContent}</Alert> : <></>}

            <div style={{width: '100%'}}>
                <ResultsDataGrid/>
            </div>

        </Box>
    )
}

