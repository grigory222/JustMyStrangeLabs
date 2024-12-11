import {DataGrid} from "@mui/x-data-grid";
import {useSelector} from "react-redux";

const columns = [
    {field: 'id', headerName: '№', flex: 1, headerAlign: 'center', align: 'center'},
    {
        field: 'x',
        headerName: 'x',
        type: 'number',
        flex: 1.5,
        editable: false,
        headerAlign: 'center',
        align: 'center',
    },
    {
        field: 'y',
        headerName: 'y',
        type: 'number',
        flex: 1.5,
        editable: false,
        headerAlign: 'center',
        align: 'center',
    },
    {
        field: 'r',
        headerName: 'r',
        type: 'number',
        flex: 1.5,
        editable: false,
        headerAlign: 'center',
        align: 'center',
    },
    {
        field: 'result',
        headerName: 'Результат',
        description: 'This column has a value getter and is not sortable.',
        sortable: false,
        flex: 2,
        headerAlign: 'center',
        align: 'center',
        //valueGetter: (value, row) => `${row.firstName || ''} ${row.lastName || ''}`,
    },
];

// const rows = [
//     {id: 1, x: 12, y: 12, r: 14, result: 'Попадание'},
//     {id: 2, x: 12, y: 12, r: 14, result: 'Промах'},
// ];

export function ResultsDataGrid() {
    const rows = useSelector(state => state.reducer.results.array);
    //const dispatcher = useDispatch();

    return (<DataGrid
        rows={rows}
        columns={columns}
        initialState={{
            pagination: {
                paginationModel: {
                    pageSize: 5,
                }
            },
        }}
        pageSizeOptions={[5]}
        disableRowSelectionOnClick
        sx={{
            mx: 15,
            my: 5,
            overflowX: 'scroll'
        }}
    />);
}