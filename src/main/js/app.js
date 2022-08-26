const React = require('react');
const ReactDOM = require('react-dom');
const HeaderForm = require('./components/HeaderForm');
const EventList = require('./components/EventList');


class App extends React.Component {
    render() {
        return (
            <>
                <HeaderForm events={1}/>
                <br/>
                <EventList/>
            </>
        );
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)