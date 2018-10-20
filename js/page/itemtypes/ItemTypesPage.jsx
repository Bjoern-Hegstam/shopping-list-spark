import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { ItemTypeType } from '../../propTypes';
import { createErrorSelector, createLoadingSelector, itemTypesSelector } from '../../selectors';
import * as types from '../../actions/types';
import AppLayout from '../../components/AppLayout';
import ItemType from './ItemType';
import { getItemTypes } from '../../actions/ItemTypeActions';

export class ItemTypesPage extends React.Component {
    static propTypes = {
        token: PropTypes.string.isRequired,
        itemTypes: PropTypes.arrayOf(ItemTypeType),

        getItemTypes: PropTypes.func.isRequired,
        fetchingItemTypes: PropTypes.bool.isRequired,
        errorGetItemTypes: PropTypes.object,
    };

    static defaultProps = {
        itemTypes: [],

        fetchingItemTypes: false,
    };

    componentDidMount() {
        this.props.getItemTypes(this.props.token);
    }

    render() {
        const { itemTypes } = this.props;

        return (
            <AppLayout>
                <div className="item-types">
                    {itemTypes.map(itemType => <ItemType key={itemType.id} {...itemType} />)}
                </div>
            </AppLayout>
        );
    }
}

const mapStateToProps = state => ({
    token: state.auth.token,
    itemTypes: itemTypesSelector(state),

    fetchingItemTypes: createLoadingSelector(types.GET_ITEM_TYPES)(state),
    errorGetItemTypes: createErrorSelector(types.GET_ITEM_TYPES)(state),
});

const mapDispatchToProps = dispatch => ({
    getItemTypes: token => dispatch(getItemTypes(token)),
});

export default connect(mapStateToProps, mapDispatchToProps)(ItemTypesPage);
