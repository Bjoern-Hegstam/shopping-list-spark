import React from 'react';
import * as PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { ItemTypeType } from '../../propTypes';
import { itemTypesSelector } from '../../selectors';
import AppLayout from '../../components/AppLayout';
import ItemType from './ItemType';
import { deleteItemType, getItemTypes } from '../../actions/ItemTypeActions';

export class ItemTypesPage extends React.Component {
    static propTypes = {
      token: PropTypes.string.isRequired,
      itemTypes: PropTypes.arrayOf(ItemTypeType),

      getItemTypes: PropTypes.func.isRequired,
      deleteItemType: PropTypes.func.isRequired,
    };

    static defaultProps = {
      itemTypes: [],
    };

    componentDidMount() {
      this.props.getItemTypes(this.props.token);
    }

    handleDeleteItemType = (id) => {
      this.props.deleteItemType({ token: this.props.token, id });
    };

    render() {
      const { itemTypes } = this.props;

      return (
        <AppLayout>
          <div className="item-types">
            {itemTypes.map(itemType => (
              <ItemType
                key={itemType.id}
                onDelete={this.handleDeleteItemType}
                {...itemType}
              />
            ))}
          </div>
        </AppLayout>
      );
    }
}

const mapStateToProps = state => ({
  token: state.auth.token,
  itemTypes: itemTypesSelector(state),
});

const mapDispatchToProps = {
  getItemTypes,
  deleteItemType,
};

export default connect(mapStateToProps, mapDispatchToProps)(ItemTypesPage);
